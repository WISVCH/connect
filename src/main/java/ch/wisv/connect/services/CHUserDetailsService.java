/*
 * Copyright 2019 W.I.S.V. 'Christiaan Huygens'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.wisv.connect.services;

import ch.wisv.connect.model.CHUserDetails;
import ch.wisv.dienst2.apiclient.model.MembershipStatus;
import ch.wisv.dienst2.apiclient.model.Person;
import ch.wisv.dienst2.apiclient.model.Student;
import ch.wisv.dienst2.apiclient.util.Dienst2Repository;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchControls;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CH User Details Service
 * <p>
 * Loads Dienst2 Person object and Google groups with Dienst2 Google username.
 */
@Service
public class CHUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CHUserDetailsService.class);

    private static final Pattern subjectPattern = Pattern.compile(CHUserDetails.SUBJECT_PREFIX + "(\\d+)");

    private final Dienst2Repository dienst2Repository;

    @Autowired
    public CHUserDetailsService(Dienst2Repository dienst2Repository) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        this.dienst2Repository = dienst2Repository;
    }

    @Override
    @CachePut(cacheNames = "userDetails", key = "#result.subject")
    @CacheEvict(cacheNames = "userInfo", key = "#result.subject")
    public CHUserDetails loadUserByUsername(String username) throws CHAuthenticationException {
        throw new CHDeprecationException();
    }

    @CachePut(cacheNames = "userDetails", key = "#result.subject")
    @CacheEvict(cacheNames = "userInfo", key = "#result.subject")
    public CHUserDetails loadUserByGoogleCredential(SAMLCredential samlCredential) throws CHAuthenticationException {
        String email = samlCredential.getNameID().getValue();
        Preconditions.checkArgument(StringUtils.isNotBlank(email), "email must not be blank");

        // The username is the email address without "@ch.tudelft.nl"
        String username = email.replace("@ch.tudelft.nl", "");

        String meta = "email=" + email + ", username=" + username;
        log.debug("Loading user by {}", meta);
        // Get the person from Dienst2
        Person person = verifyMembership(dienst2Repository.getPersonFromGoogleUsername(username), meta);

        return createUserDetails(person, CHUserDetails.AuthenticationSource.GOOGLE_SSO);
    }

    /**
     * Look up user by NetID and student number.
     * <p>
     * Objects from Dienst2 are checked for conflicts. This code assumes exact matching and uniqueness constraints for
     * NetID and student number in Dienst2.
     *
     * @param netid         NetID to look for
     * @param studentNumber Student number to look for
     * @param study         Study to write back to Dienst2
     * @return user details object
     * @throws CHAuthenticationException when membership could not be validated
     */
    @CachePut(cacheNames = "userDetails", key = "#result.subject")
    @CacheEvict(cacheNames = "userInfo", key = "#result.subject")
    public CHUserDetails loadUserByNetidStudentNumberAndUpdateStudy(String netid, String studentNumber, String study) throws
            CHAuthenticationException {
        Preconditions.checkArgument(StringUtils.isNotBlank(netid), "netid must not be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(studentNumber), "studentNumber must not be blank");
        String meta = String.format("netid=%s studentNumber=%s", netid, studentNumber);
        log.debug("Loading user by {}", meta);

        Optional<Person> personFromNetid = dienst2Repository.getPersonFromNetid(netid);

        // If this person also has the student number we are looking for we can be sure that there is no conflict on
        // student number, due to the uniqueness constraint on student number in Dienst2. This saves us a REST request
        // to Dienst2. Else, we do have to look up by student number in Dienst2.
        boolean matchesStudentNumber = personFromNetid.flatMap(Person::getStudent)
                .map(Student::getStudentNumber)
                .map(s -> s.equals(studentNumber))
                .orElse(false);
        Optional<Person> personFromStudentNumber = matchesStudentNumber ? personFromNetid :
                dienst2Repository.getPersonFromStudentNumber(studentNumber);

        if (personFromNetid.isPresent() && personFromStudentNumber.isPresent()) {
            Person person = verifyMembership(personFromNetid, meta);
            if (!personFromStudentNumber.get().getId().equals(personFromNetid.get().getId())) {
                // NetID and student number match a different person: conflict
                log.warn("Conflict: dienst2personId={} matching student number is not dienst2PersonId={} matching NetID",
                        personFromStudentNumber.get().getId(), personFromNetid.get().getId());
                throw new CHMemberConflictException();
            } else {
                person = updateDienst2(person, netid, studentNumber, study);
                return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
            }
        } else if (personFromNetid.isPresent()) {
            Person person = verifyMembership(personFromNetid, meta);
            String dienst2StudentNumber = person.getStudent().map(Student::getStudentNumber).orElse("");
            if (StringUtils.isNotBlank(dienst2StudentNumber)) {
                // If Dienst2 student number is set, it should have matched earlier: conflict
                log.warn("Conflict: dienst2StudentNumber={} does not match samlStudentNumber={}",
                        dienst2StudentNumber, studentNumber);
                throw new CHMemberConflictException();
            } else {
                person = updateDienst2(person, netid, studentNumber, study);
                return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
            }
        } else if (personFromStudentNumber.isPresent()) {
            Person person = verifyMembership(personFromStudentNumber, meta);
            String dienst2Netid = person.getNetid();
            if (StringUtils.isNotBlank(dienst2Netid)) {
                // If Dienst2 NetID is set, it should have matched earlier: conflict
                log.warn("Conflict: dienst2Netid={} does not match samlNetid={}", dienst2Netid, netid);
                throw new CHMemberConflictException();
            } else {
                person = updateDienst2(person, netid, studentNumber, study);
                return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
            }
        } else {
            // No matches: invalid member
            log.warn("Invalid: no dienst2 matches for netid={} studentNumber={}", netid, studentNumber);
            throw new CHInvalidMemberException();
        }
    }

    /**
     * Look up user by NetID
     *
     * @param netid NetID to look for
     * @return user details object
     * @throws CHAuthenticationException when membership could not be validated
     */
    @CachePut(cacheNames = "userDetails", key = "#result.subject")
    @CacheEvict(cacheNames = "userInfo", key = "#result.subject")
    public CHUserDetails loadUserByNetid(String netid) throws CHAuthenticationException {
        Preconditions.checkArgument(StringUtils.isNotBlank(netid), "netid must not be blank");
        String meta = "netid=" + netid;
        log.debug("Loading user by {}", meta);

        Optional<Person> personFromNetid = dienst2Repository.getPersonFromNetid(netid);
        Person person = verifyMembership(personFromNetid, meta);
        return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
    }

    @Cacheable("userDetails")
    @CacheEvict(cacheNames = "userInfo", key = "#result.subject")
    public CHUserDetails loadUserBySubject(String subject) throws CHAuthenticationException {
        Preconditions.checkArgument(StringUtils.isNotBlank(subject), "subject must not be blank");
        String meta = "subject=" + subject;
        log.debug("Loading user by {}", meta);
        Matcher subjectMatcher = subjectPattern.matcher(subject);
        if (!subjectMatcher.matches()) {
            log.warn("Subject subject={} does not match pattern", subject);
            return null;
        }
        int id = Integer.parseInt(subjectMatcher.group(1));
        Person person = verifyMembership(dienst2Repository.getPerson(id), meta);
        return createUserDetails(person, null);
    }

    private CHUserDetails createUserDetails(Person person,
                                                  CHUserDetails.AuthenticationSource authenticationSource) {
        assert person != null;

        String googleUsername = person.getGoogleUsername();
        System.out.println("googleUsername: " + googleUsername);
        Set<String> googleGroups = Collections.emptySet();
        if (StringUtils.isNotEmpty(googleUsername)) {
            googleGroups = dienst2Repository.getGoogleGroups(person.getId());
        }

        return new CHUserDetails(person, googleGroups, authenticationSource);
    }

    private Person verifyMembership(Optional<Person> person, String meta) {
        if (!person.isPresent()) {
            log.warn("No Dienst2 person found for {}", meta);
            throw new CHInvalidMemberException();
        }
        Person p = person.get();
        MembershipStatus membershipStatus = p.getMembershipStatus();
        if (membershipStatus.getValue() < MembershipStatus.REGULAR.getValue()) {
            log.warn("Person dienst2Id={} is not a valid member (status {}, found by {})",
                    p.getId(), membershipStatus, meta);
            throw new CHInvalidMemberException();
        }
        return p;
    }

    private Person updateDienst2(Person person, String netid, String studentNumber, String study) {
        Person patch = new Person();
        List<String> diff = new ArrayList<>(3);
        if (!netid.equals(person.getNetid())) {
            patch.setNetid(netid);
            diff.add("NetID");
        }
        if (!person.getStudent().map(Student::getStudentNumber).map(s -> s.equals(studentNumber)).orElse(false)) {
            Student s = new Student();
            patch.setStudent(s);
            s.setStudentNumber(studentNumber);
            diff.add("student number");
        }
        if (!person.getStudent().map(Student::getStudy).map(s -> s.equals(study)).orElse(false)) {
            Student s = patch.getStudent().orElse(new Student());
            patch.setStudent(s);
            s.setStudy(study);
            diff.add("study");
        }
        if (diff.isEmpty()) {
            log.debug("Dienst2 already up to date");
            return person;
        }

        String revisionComment = "Update " + String.join(", ", diff) + " from NetID login";
        log.debug("Updating person {} in Dienst2: {}", person.getId(), revisionComment);
        patch.setRevisionComment(revisionComment);

        try {
            return dienst2Repository.patchPerson(person.getId(), patch).orElse(person);
        } catch (Exception e) {
            log.warn("Could not update Dienst2 from NetID login", e);
            return person;
        }
    }

    public abstract static class CHAuthenticationException extends UsernameNotFoundException {
        public CHAuthenticationException(String msg) {
            super(msg);
        }
    }

    public static class CHInvalidMemberException extends CHAuthenticationException {
        public CHInvalidMemberException() {
            super("Not a valid CH member");
        }
    }

    public static class CHPreStudentAuthenticatedException extends CHAuthenticationException {
        public CHPreStudentAuthenticatedException() {
            super("Prospective student has authenticated, please wait until the academic year has started");
        }
    }

    public static class CHMemberConflictException extends CHAuthenticationException {
        public CHMemberConflictException() {
            super("Conflict between NetID and student number");
        }
    }

    public static class CHDeprecationException extends CHAuthenticationException {
        public CHDeprecationException() {
            super("This service is deprecated");
        }
    }
}
