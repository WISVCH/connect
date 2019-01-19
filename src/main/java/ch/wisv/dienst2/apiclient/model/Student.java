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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Member model
 */
public class Student implements Serializable {
    private int person;

    private String study;
    private int firstYear;
    private String studentNumber;
    private boolean enrolled;

    private String phoneParents;

    private boolean yearbookPermission;

    private LocalDate dateVerified;

    public int getPerson() {
        return person;
    }

    public String getStudy() {
        return study;
    }

    public int getFirstYear() {
        return firstYear;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public String getPhoneParents() {
        return phoneParents;
    }

    public boolean isYearbookPermission() {
        return yearbookPermission;
    }

    public LocalDate getDateVerified() {
        return dateVerified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(person, student.person) &&
                Objects.equals(firstYear, student.firstYear) &&
                Objects.equals(enrolled, student.enrolled) &&
                Objects.equals(yearbookPermission, student.yearbookPermission) &&
                Objects.equals(study, student.study) &&
                Objects.equals(studentNumber, student.studentNumber) &&
                Objects.equals(phoneParents, student.phoneParents) &&
                Objects.equals(dateVerified, student.dateVerified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, study, firstYear, studentNumber, enrolled, phoneParents, yearbookPermission,
                dateVerified);
    }
}
