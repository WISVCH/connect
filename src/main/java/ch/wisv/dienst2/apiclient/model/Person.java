/*
 * Copyright 2019 W.I.S.V. 'Christiaan Huygens'
 * Copyright 2018 The MITRE Corporation
 *    and the MIT Internet Trust Consortium
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

package ch.wisv.dienst2.apiclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Person model
 */
public class Person extends Entity implements Serializable {
    private String formattedName;
    private String titles;
    private String initials;
    private String firstname;
    private String preposition;
    private String surname;
    private String postfixTitles;

    private String phoneMobile;

    private String gender;
    private LocalDate birthdate;
    private boolean deceased;
    private URI livingWith;

    private boolean mailAnnouncements;
    private boolean mailCompany;

    private String ldapUsername;
    private String netid;
    private String facebookId;

    private MembershipStatus membershipStatus;

    private Member member;
    private Student student;
    private Alumnus alumnus;
    private Employee employee;

    @JsonProperty("committee_memberships")
    private List<CommitteeMembership> committeeMemberships;

    public String getFormattedName() {
        return formattedName;
    }

    public List<CommitteeMembership> getCommitteeMemberships() {
        return committeeMemberships;
    }

    public String getTitles() {
        return titles;
    }

    public String getInitials() {
        return initials;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPreposition() {
        return preposition;
    }

    public String getSurname() {
        return surname;
    }

    public String getSurnameWithPreposition() {
        if (!StringUtils.isEmpty(getPreposition())) {
            return getPreposition() + " " + getSurname();
        } else {
            return getSurname();
        }
    }

    public String getPostfixTitles() {
        return postfixTitles;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public boolean isDeceased() {
        return deceased;
    }

    public Optional<URI> getLivingWith() {
        return Optional.ofNullable(livingWith);
    }

    public boolean isMailAnnouncements() {
        return mailAnnouncements;
    }

    public boolean isMailCompany() {
        return mailCompany;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public String getNetid() {
        return netid;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public Optional<Member> getMember() {
        return Optional.ofNullable(member);
    }

    public Optional<Student> getStudent() {
        return Optional.ofNullable(student);
    }

    public Optional<Alumnus> getAlumnus() {
        return Optional.ofNullable(alumnus);
    }

    public Optional<Employee> getEmployee() {
        return Optional.ofNullable(employee);
    }

    public List<CommitteeMembership> getCommittees() {
        return committeeMemberships;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(deceased, person.deceased) &&
                Objects.equals(mailAnnouncements, person.mailAnnouncements) &&
                Objects.equals(mailCompany, person.mailCompany) &&
                Objects.equals(titles, person.titles) &&
                Objects.equals(initials, person.initials) &&
                Objects.equals(firstname, person.firstname) &&
                Objects.equals(preposition, person.preposition) &&
                Objects.equals(surname, person.surname) &&
                Objects.equals(postfixTitles, person.postfixTitles) &&
                Objects.equals(phoneMobile, person.phoneMobile) &&
                Objects.equals(gender, person.gender) &&
                Objects.equals(birthdate, person.birthdate) &&
                Objects.equals(livingWith, person.livingWith) &&
                Objects.equals(ldapUsername, person.ldapUsername) &&
                Objects.equals(netid, person.netid) &&
                Objects.equals(facebookId, person.facebookId) &&
                Objects.equals(member, person.member) &&
                Objects.equals(student, person.student) &&
                Objects.equals(alumnus, person.alumnus) &&
                Objects.equals(employee, person.employee) &&
                Objects.equals(committeeMemberships, person.committeeMemberships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), titles, initials, firstname, preposition, surname, postfixTitles,
                phoneMobile, gender, birthdate, deceased, livingWith, mailAnnouncements, mailCompany, ldapUsername,
                netid, facebookId, member, student, alumnus, employee, committeeMemberships);
    }

}
