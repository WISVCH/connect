package ch.wisv.connect.overlay.model;

import ch.wisv.dienst2.apiclient.model.Person;
import com.google.common.collect.ImmutableSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * CH User Details
 */
public class CHUserDetails implements UserDetails {
    public static final String USERNAME_PREFIX = "WISVCH.";

    private Person person;
    private Set<String> ldapGroups;
    private AuthenticationSource authenticationSource;

    private static final GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    private static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    public CHUserDetails(Person person, Set<String> ldapGroups, AuthenticationSource authenticationSource) {
        this.person = person;
        this.ldapGroups = ldapGroups;
        this.authenticationSource = authenticationSource;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We do not fully trust TU Delft SSO, so we only grant admin authority if logged on through CH LDAP.
        if (ldapGroups.contains("staff") && authenticationSource == AuthenticationSource.CH_LDAP) {
            return ImmutableSet.of(ROLE_ADMIN, ROLE_USER);
        } else {
            return ImmutableSet.of(ROLE_USER);
        }
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return USERNAME_PREFIX + person.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Person getPerson() {
        return person;
    }

    public Set<String> getLdapGroups() {
        return ldapGroups;
    }

    public enum AuthenticationSource {
        CH_LDAP, TU_SSO
    }
}
