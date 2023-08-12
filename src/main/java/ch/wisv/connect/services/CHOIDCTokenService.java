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

package ch.wisv.connect.services;

import com.nimbusds.jwt.JWTClaimsSet;
import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.OAuth2AccessTokenEntity;
import org.mitre.openid.connect.service.impl.DefaultOIDCTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Set;

public class CHOIDCTokenService extends DefaultOIDCTokenService {

    private final CHUserDetailsService chUserDetailsService;

    @Autowired
    public CHOIDCTokenService(CHUserDetailsService chUserDetailsService) {
        this.chUserDetailsService = chUserDetailsService;
    }

    @Override
    protected void addCustomIdTokenClaims(JWTClaimsSet.Builder idClaims, ClientDetailsEntity client, OAuth2Request request, String sub, OAuth2AccessTokenEntity accessToken) {
        if (request.getScope().contains("auth")) {
            Set<String> googleGroups = chUserDetailsService.loadUserBySubject(sub).getGoogleGroups();
            idClaims.claim("google_groups", googleGroups);
        }
    }

}
