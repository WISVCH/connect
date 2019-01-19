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
 * Alumnus model
 */
public class Alumnus implements Serializable {
    private int person;

    private String study;
    private int studyFirstYear;
    private int studyLastYear;
    private String researchGroup;
    private String paper;
    private String professor;

    private String workCompany;
    private String position;
    private String sector;

    private char contactMethod;

    public int getPerson() {
        return person;
    }

    public String getStudy() {
        return study;
    }

    public int getStudyFirstYear() {
        return studyFirstYear;
    }

    public int getStudyLastYear() {
        return studyLastYear;
    }

    public String getResearchGroup() {
        return researchGroup;
    }

    public String getPaper() {
        return paper;
    }

    public String getProfessor() {
        return professor;
    }

    public String getWorkCompany() {
        return workCompany;
    }

    public String getPosition() {
        return position;
    }

    public String getSector() {
        return sector;
    }

    public char getContactMethod() {
        return contactMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Alumnus alumnus = (Alumnus) o;
        return Objects.equals(person, alumnus.person) &&
                Objects.equals(studyFirstYear, alumnus.studyFirstYear) &&
                Objects.equals(studyLastYear, alumnus.studyLastYear) &&
                Objects.equals(contactMethod, alumnus.contactMethod) &&
                Objects.equals(study, alumnus.study) &&
                Objects.equals(researchGroup, alumnus.researchGroup) &&
                Objects.equals(paper, alumnus.paper) &&
                Objects.equals(professor, alumnus.professor) &&
                Objects.equals(workCompany, alumnus.workCompany) &&
                Objects.equals(position, alumnus.position) &&
                Objects.equals(sector, alumnus.sector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, study, studyFirstYear, studyLastYear, researchGroup, paper, professor,
                workCompany, position, sector, contactMethod);
    }
}
