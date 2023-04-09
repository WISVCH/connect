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

package ch.wisv.connect.services;

import ch.wisv.connect.model.CHUserDetails;
import ch.wisv.connect.model.CHUserInfo;
import ch.wisv.dienst2.apiclient.model.Person;
import ch.wisv.dienst2.apiclient.model.Student;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.mitre.openid.connect.model.Address;
import org.mitre.openid.connect.model.DefaultAddress;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * OIDC User Info repository
 */
public class CHUserInfoRepository implements UserInfoRepository {
    private static final Logger log = LoggerFactory.getLogger(CHUserInfoRepository.class);

    @Autowired
    CHUserDetailsService chUserDetailsService;

    @Override
    @Cacheable("userInfo")
    public UserInfo getByUsername(String subject) {
        log.debug("Loading user info for subject={}", subject);
        CHUserDetails userDetails = chUserDetailsService.loadUserBySubject(subject);
        return mapUserDetailsToUserInfo(userDetails);
    }

    @Override
    public UserInfo getByEmailAddress(String email) {
        throw new NotImplementedException();
    }

    private static UserInfo mapUserDetailsToUserInfo(CHUserDetails userDetails) {
        Person person = userDetails.getPerson();
        CHUserInfo ui = new CHUserInfo();
        ui.setSub(userDetails.getUsername());
        String preferredUsername = StringUtils.isNotBlank(person.getLdapUsername()) ? person.getLdapUsername() :
                person.getNetid();
        ui.setPreferredUsername(preferredUsername);
        ui.setEmail(person.getEmail());
        ui.setName(person.getFormattedName());
        ui.setGivenName(person.getFirstname());
        ui.setFamilyName(person.getSurnameWithPreposition());
        Address address = new DefaultAddress();
        address.setStreetAddress(person.getStreetAddress());
        address.setPostalCode(person.getPostcode());
        address.setLocality(person.getCity());
        address.setCountry(person.getCountry());
        address.setFormatted(person.getFormattedAddress());
        ui.setAddress(address);
        ui.setBirthdate(Optional.ofNullable(person.getBirthdate()).map(LocalDate::toString).orElse(null));
        switch (person.getGender()) {
            case "M":
                ui.setGender("male");
                break;
            case "F":
                ui.setGender("female");
                break;
        }
        ui.setPhoneNumber(person.getPhoneMobile());
        ui.setLdapUsername(person.getLdapUsername());
        ui.setLdapGroups(userDetails.getLdapGroups());
        ui.setGoogleUsername(person.getGoogleUsername());
        ui.setGoogleGroups(userDetails.getGoogleGroups());
        ui.setNetid(person.getNetid());
        if (person.getStudent().map(Student::isEnrolled).orElse(false)) {
            ui.setStudentNumber(person.getStudent().map(Student::getStudentNumber).orElse(null));
            ui.setStudy(person.getStudent().map(Student::getStudy).orElse(null));
        }
        return ui;
    }
}
