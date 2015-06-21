package ch.wisv.connect.authentication;

import ch.wisv.dienst2.apiclient.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * CH User Details
 */
public class CHUserDetails implements UserDetails {
    private Person person;
    private Set<String> ldapGroups;

    public CHUserDetails(Person person, Set<String> ldapGroups) {
        this.person = person;
        this.ldapGroups = ldapGroups;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return "WISVCH." + person.getId();
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
}
