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

package ch.wisv.connect.authentication;

import ch.wisv.connect.model.CHUserDetails;
import ch.wisv.connect.services.CHUserDetailsService;
import ch.wisv.connect.services.CHUserDetailsService.CHInvalidMemberException;
import datadog.trace.api.Trace;
import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.saml.SAMLCredential;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CH Authentication Provider
 */
public class CHAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(CHAuthenticationProvider.class);
    private static final String SAML_ATTRIBUTE_AFFILIATION = "urn:mace:dir:attribute-def:eduPersonAffiliation";
    private static final String SAML_ATTRIBUTE_STUDENTNUMBER = "tudStudentNumber";

    @Autowired
    CHUserDetailsService userDetailService;

    @Trace
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object credentials = authentication.getCredentials();
        Object principal = authentication.getPrincipal();

        if (credentials instanceof SAMLCredential) {
            SAMLCredential samlCredential = (SAMLCredential) credentials;
            List<Attribute> attributes = samlCredential.getAttributes();
            String username = authentication.getName();
            if (log.isDebugEnabled()) {
                String attributesString = attributes.stream().map(Attribute::getName).map(n ->
                        String.format("%s=\"%s\"", n, samlCredential.getAttributeAsString(n)))
                        .collect(Collectors.joining(" "));
                log.info("Authenticated via SAML: username={} {}", username, attributesString);
            }

            if (!username.endsWith("@tudelft.nl")) {
                log.warn("Unsupported SAML username suffix: username={}", username);
                throw new CHInvalidMemberException();
            }
            String netid = username.substring(0, username.indexOf('@'));

            CHUserDetails userDetails;
            String affiliation = samlCredential.getAttributeAsString(SAML_ATTRIBUTE_AFFILIATION);
            switch (affiliation) {
                case "student":
                    String studentNumber = samlCredential.getAttributeAsString(SAML_ATTRIBUTE_STUDENTNUMBER);
                    userDetails = userDetailService.loadUserByNetidStudentNumber(netid, studentNumber);
                    break;
                case "employee":
                    userDetails = userDetailService.loadUserByNetid(netid);
                    break;
                default:
                    log.warn("Unsupported SAML affiliation: affiliation={}", affiliation);
                    throw new CHInvalidMemberException();
            }

            return CHAuthenticationToken.createAuthenticationToken(authentication, userDetails);
        } else if (principal instanceof LdapUserDetails) {
            LdapUserDetails ldapUserDetails = (LdapUserDetails) principal;
            String ldapUsername = ldapUserDetails.getUsername();
            log.info("Authenticated via LDAP: username={}", ldapUsername);

            CHUserDetails userDetails = userDetailService.loadUserByUsername(ldapUsername);
            return CHAuthenticationToken.createAuthenticationToken(authentication, userDetails);
        } else {
            log.debug("Invalid authentication object type");
            throw new IllegalArgumentException("Invalid authentication object type");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
