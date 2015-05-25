package samltest;

import ch.wisv.dienst2.apiclient.model.Person;
import ch.wisv.dienst2.apiclient.model.Results;
import ch.wisv.dienst2.apiclient.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Dienst2 repository
 */
@Repository
public class Dienst2Repository {
    @Value("${dienst2.baseurl}")
    private String BASEURL;

    @Autowired
    private RestTemplate restTemplate;

    public static final ParameterizedTypeReference<Results<Person>> PERSON_RESPONSE_TYPE = new ParameterizedTypeReference<Results<Person>>() {
    };

    public Optional<Person> getPersonFromLdapUsername(String ldapUsername) {
        String url = BASEURL + "/ldb/api/v3/people/?ldap_username={username}";
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, PERSON_RESPONSE_TYPE, ldapUsername);
        return getAtMostOneResult(e);
    }

    public Optional<Person> getPersonFromNetid(String netid) {
        String url = BASEURL + "/ldb/api/v3/people/?netid=x{netid}";
        ParameterizedTypeReference<Results<Person>> responseType = new ParameterizedTypeReference<Results<Person>>() {
        };
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, responseType, netid);
        return getAtMostOneResult(e);
    }

    public Optional<Person> getPersonFromStudentNumber(String studentNumber) {
        String url = BASEURL + "/ldb/api/v3/people/?student__student_number={studentNumber}";
        ParameterizedTypeReference<Results<Person>> responseType = new ParameterizedTypeReference<Results<Person>>() {
        };
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, responseType, studentNumber);
        return getAtMostOneResult(e);
    }

    public Person getPerson(Student student) {
        return restTemplate.getForObject(BASEURL + student.getPerson(), Person.class);
    }

    private <T> Optional<T> getAtMostOneResult(ResponseEntity<Results<T>> e) {
        Results<T> result = e.getBody();
        int size = result.getResults().size();
        if (size == 0)
            return Optional.empty();
        else if (size == 1)
            return Optional.of(result.getResults().get(0));
        else
            throw new IncorrectResultSizeDataAccessException(size, 1);
    }

}
