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

package ch.wisv.dienst2.apiclient.model;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
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

    private String ldapUsername;
    private String netid;

    private MembershipStatus membershipStatus;

    private Student student;

    public String getFormattedName() {
        return formattedName;
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

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public Optional<Student> getStudent() {
        return Optional.ofNullable(student);
    }

    public void setStudent(Student student) {
        this.student = student;
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
        return Objects.equals(formattedName, person.formattedName) &&
                Objects.equals(titles, person.titles) &&
                Objects.equals(initials, person.initials) &&
                Objects.equals(firstname, person.firstname) &&
                Objects.equals(preposition, person.preposition) &&
                Objects.equals(surname, person.surname) &&
                Objects.equals(postfixTitles, person.postfixTitles) &&
                Objects.equals(phoneMobile, person.phoneMobile) &&
                Objects.equals(gender, person.gender) &&
                Objects.equals(birthdate, person.birthdate) &&
                Objects.equals(ldapUsername, person.ldapUsername) &&
                Objects.equals(netid, person.netid) &&
                membershipStatus == person.membershipStatus &&
                Objects.equals(student, person.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), formattedName, titles, initials, firstname, preposition, surname, postfixTitles, phoneMobile, gender, birthdate, ldapUsername, netid, membershipStatus, student);
    }
}
