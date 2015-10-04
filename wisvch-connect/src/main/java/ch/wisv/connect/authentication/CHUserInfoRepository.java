package ch.wisv.connect.authentication;

import ch.wisv.dienst2.apiclient.model.Person;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.mitre.openid.connect.model.Address;
import org.mitre.openid.connect.model.DefaultAddress;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OIDC User Info repository
 */
public class CHUserInfoRepository implements UserInfoRepository {
    private static final Logger log = LoggerFactory.getLogger(CHUserInfoRepository.class);

    private static final Pattern usernamePattern = Pattern.compile(CHUserDetails.USERNAME_PREFIX + "(\\d+)");

    @Autowired
    CHUserDetailsService chUserDetailsService;

    @Override
    public UserInfo getByUsername(String username) {
        log.debug("Loading user info for {}", username);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CHUserDetails) {
            CHUserDetails userDetails = (CHUserDetails) principal;
            if (userDetails.getUsername().equals(username)) {
                log.debug("User details from authentication object match");
                return mapUserDetailsToUserInfo(userDetails);
            } else {
                // Not sure if there is a case where an authenticated user loads user info for someone else, so log this
                log.warn("User details from authentication object DO NOT match");
            }
        }

        Matcher usernameMatcher = usernamePattern.matcher(username);
        if (!usernameMatcher.matches()) {
            log.debug("Username did not match pattern");
            return null;
        }

        int id = Integer.parseInt(usernameMatcher.group(1));
        log.debug("Loading user info from service by id {}", id);
        CHUserDetails userDetails = chUserDetailsService.loadUserById(id);
        return mapUserDetailsToUserInfo(userDetails);
    }

    @Override
    public UserInfo getByEmailAddress(String email) {
        throw new NotImplementedException();
    }

    private static UserInfo mapUserDetailsToUserInfo(CHUserDetails userDetails) {
        // TODO: Extend user details with ldap username, ldap groups, netid, student number, etc.
        Person person = userDetails.getPerson();
        UserInfo ui = new DefaultUserInfo();
        ui.setSub(userDetails.getUsername());
        String preferredUsername = StringUtils.isNotBlank(person.getLdapUsername()) ? person.getLdapUsername() :
                person.getNetid();
        ui.setPreferredUsername(preferredUsername);
        ui.setEmail(person.getEmail());
        ui.setName(person.getFormattedName());
        ui.setGivenName(person.getFirstname());
        ui.setFamilyName(person.getSurname());
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
        return ui;
    }
}
