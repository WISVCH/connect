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
    private RestTemplate restTemplate;
    private String baseUrl;

    public Dienst2Repository(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public static final ParameterizedTypeReference<Results<Person>> PERSON_RESULT_TYPE = new
            ParameterizedTypeReference<Results<Person>>() {
            };

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
        String url = baseUrl + "/ldb/api/v3/people/{id}/";
        try {
            return Optional.of(restTemplate.getForObject(url, Person.class, id));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
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
