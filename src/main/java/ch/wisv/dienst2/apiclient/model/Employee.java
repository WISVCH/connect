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
import java.util.Objects;

/**
 * Employee model
 */
public class Employee implements Serializable {
    private int person;

    private String faculty;
    private String department;
    private String function;
    private String phoneInternal;

    public int getPerson() {
        return person;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDepartment() {
        return department;
    }

    public String getFunction() {
        return function;
    }

    public String getPhoneInternal() {
        return phoneInternal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(person, employee.person) &&
                Objects.equals(faculty, employee.faculty) &&
                Objects.equals(department, employee.department) &&
                Objects.equals(function, employee.function) &&
                Objects.equals(phoneInternal, employee.phoneInternal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, faculty, department, function, phoneInternal);
    }
}
