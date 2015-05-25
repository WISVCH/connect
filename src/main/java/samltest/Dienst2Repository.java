package samltest;

import ch.wisv.dienst2.apiclient.model.Member;
import ch.wisv.dienst2.apiclient.model.Person;
import ch.wisv.dienst2.apiclient.model.Results;
import ch.wisv.dienst2.apiclient.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

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

    // TODO: Wel of geen exceptions?
    // TODO: onderscheid tussen overwachte fouten (API niet bereikbaar) of verwachte fouten (geen lid)
    // TODO: verifyMembership naar CHAuthenticationProvider
    public int verifyMembershipFromLdapUsername(String ldapUsername) throws Dienst2Exception {
        Person person = getPersonFromLdapUsername(ldapUsername);
        return verifyMembership(person);
    }

    public int verifyMembershipFromStudentNumber(String studentNumber) throws Dienst2Exception {
        Person person = getPersonFromStudentNumber(studentNumber);
        return verifyMembership(person);
    }

    public int verifyMembershipFromNetid(String netid) throws Dienst2Exception {
        Person person = getPersonFromNetid(netid);
        return verifyMembership(person);
    }

    public int verifyMembership(Person person) throws Dienst2Exception {
        return person.getMember().filter(Member::isCurrentMember).map(Member::getPerson)
                .orElseThrow(() -> new Dienst2Exception("Not a current member"));
    }

    public Person getPersonFromLdapUsername(String ldapUsername) throws Dienst2Exception {
        String url = BASEURL + "/ldb/api/v3/people/?ldap_username={username}";
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, PERSON_RESPONSE_TYPE, ldapUsername);
        return singleResult(e);
    }

    public Person getPersonFromNetid(String netid) throws Dienst2Exception {
        String url = BASEURL + "/ldb/api/v3/people/?netid={netid}";
        ParameterizedTypeReference<Results<Person>> responseType = new ParameterizedTypeReference<Results<Person>>() {
        };
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, responseType, netid);
        return singleResult(e);
    }

    public Person getPersonFromStudentNumber(String studentNumber) throws Dienst2Exception {
        String url = BASEURL + "/ldb/api/v3/people/?student__student_number={studentNumber}";
        ParameterizedTypeReference<Results<Person>> responseType = new ParameterizedTypeReference<Results<Person>>() {
        };
        ResponseEntity<Results<Person>> e = restTemplate.exchange(url, HttpMethod.GET, null, responseType, studentNumber);
        return singleResult(e);
    }

    public Person getPerson(Student student) {
        return restTemplate.getForObject(BASEURL + student.getPerson(), Person.class);
    }

    private <T> T singleResult(ResponseEntity<Results<T>> e) throws Dienst2Exception {
        if (e.getStatusCode() != HttpStatus.OK)
            throw new Dienst2Exception("Invalid response");

        Results<T> result = e.getBody();
        int size = result.getResults().size();
        if (size == 0)
            throw new Dienst2Exception("Not found");
        else if (size > 1)
            throw new Dienst2Exception("Too many results");

        return result.getResults().get(0);
    }

    public class Dienst2Exception extends Exception {
        public Dienst2Exception(String message) {
            super(message);
        }

        public Dienst2Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
