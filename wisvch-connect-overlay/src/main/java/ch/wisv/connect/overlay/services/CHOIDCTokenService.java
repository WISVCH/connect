package ch.wisv.connect.overlay.services;

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
        Set<String> ldapGroups = chUserDetailsService.loadUserBySubject(sub).getLdapGroups();
        idClaims.claim("ldap_groups", ldapGroups);
    }

}
