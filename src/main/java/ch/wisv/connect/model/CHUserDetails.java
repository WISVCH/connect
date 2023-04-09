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

package ch.wisv.connect.model;

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
    public static final String SUBJECT_PREFIX = "WISVCH.";

    private Person person;
    private Set<String> ldapGroups;

    private Set<String> googleGroups;
    private AuthenticationSource authenticationSource;

    private static final GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
    private static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    public CHUserDetails(Person person, Set<String> ldapGroups, Set<String> googleGroups, AuthenticationSource authenticationSource) {
        this.person = person;
        this.ldapGroups = ldapGroups;
        this.googleGroups = googleGroups;
        this.authenticationSource = authenticationSource;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // We do not fully trust TU Delft SSO, so we only grant admin authority if logged on through CH LDAP or
        // Google SSO
        if ((ldapGroups.contains("staff") || googleGroups.contains("staff")) && (authenticationSource == AuthenticationSource.CH_LDAP ||
                authenticationSource == AuthenticationSource.GOOGLE_SSO)) {
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
        return getSubject();
    }

    public String getSubject() {
        return SUBJECT_PREFIX + person.getId();
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

    public Set<String> getGoogleGroups() {
        return googleGroups;
    }

    public enum AuthenticationSource {
        CH_LDAP, TU_SSO, GOOGLE_SSO
    }
}
