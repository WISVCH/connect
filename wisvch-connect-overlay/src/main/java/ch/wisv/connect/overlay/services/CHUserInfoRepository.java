package ch.wisv.connect.overlay.services;

import ch.wisv.connect.common.model.CHUserInfo;
import ch.wisv.connect.overlay.model.CHUserDetails;
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
        ui.setBirthdate(person.getBirthdate().toString());
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
        ui.setNetid(person.getNetid());
        ui.setStudentNumber(person.getStudent().map(Student::getStudentNumber).orElse(null));
        return ui;
    }
}
