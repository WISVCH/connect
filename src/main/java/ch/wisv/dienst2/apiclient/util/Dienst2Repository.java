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

package ch.wisv.dienst2.apiclient.util;

import ch.wisv.dienst2.apiclient.model.Person;
import ch.wisv.dienst2.apiclient.model.Results;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Dienst2 repository
 */
@SuppressWarnings("unused")
public class Dienst2Repository {
    public static final ParameterizedTypeReference<Results<Person>> PERSON_RESULT_TYPE = new
            ParameterizedTypeReference<Results<Person>>() {
            };
    private RestTemplate restTemplate;
    private String baseUrl;
    private String personBaseurl;

    public Dienst2Repository(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.personBaseurl = baseUrl + "/ldb/api/v3/people/{id}/";
    }

    public Optional<Person> getPersonFromLdapUsername(String ldapUsername) {
        String url = baseUrl + "/ldb/api/v3/people/?ldap_username={username}";
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, PERSON_RESULT_TYPE,
                ldapUsername);
        return getAtMostOneResult(e);
    }

    public Optional<Person> getPersonFromNetid(String netid) {
        String url = baseUrl + "/ldb/api/v3/people/?netid={netid}";
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, PERSON_RESULT_TYPE,
                netid);
        return getAtMostOneResult(e);
    }

    public Optional<Person> getPersonFromStudentNumber(String studentNumber) {
        String url = baseUrl + "/ldb/api/v3/people/?student__student_number={studentNumber}";
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, PERSON_RESULT_TYPE,
                studentNumber);
        return getAtMostOneResult(e);
    }

    public Optional<Person> getPerson(int id) {
        try {
            return Optional.of(restTemplate.getForObject(personBaseurl, Person.class, id));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }

    public Optional<Person> patchPerson(int id, Person person) {
        return Optional.of(restTemplate.patchForObject(personBaseurl, person, Person.class, id));
    }

    private <T> Optional<T> getAtMostOneResult(ResponseEntity<Results<T>> e) {
        Results<T> result = e.getBody();
        int size = result.getResults().size();
        if (size == 1) {
            return Optional.of(result.getResults().get(0));
        } else {
            return Optional.empty();
        }
    }

}
