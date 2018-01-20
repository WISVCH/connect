package ch.wisv.connect.overlay.services;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nimbusds.jose.*;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.*;
import org.mitre.jwt.encryption.service.JWTEncryptionAndDecryptionService;
import org.mitre.jwt.signer.service.JWTSigningAndValidationService;
import org.mitre.jwt.signer.service.impl.ClientKeyCacheService;
import org.mitre.jwt.signer.service.impl.SymmetricKeyJWTValidatorCacheService;
import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.OAuth2AccessTokenEntity;
import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.mitre.openid.connect.service.impl.DefaultOIDCTokenService;
import org.mitre.openid.connect.util.IdTokenHashUtils;
import org.mitre.openid.connect.web.AuthenticationTimeStamper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.mitre.openid.connect.request.ConnectRequestParameters.MAX_AGE;
import static org.mitre.openid.connect.request.ConnectRequestParameters.NONCE;

public class CHOIDCTokenService extends DefaultOIDCTokenService {

    private static final Logger logger = LoggerFactory.getLogger(CHOIDCTokenService.class);

    @Autowired
    private JWTSigningAndValidationService jwtService;

    @Autowired
    private ConfigurationPropertiesBean configBean;

    @Autowired
    private ClientKeyCacheService encrypters;

    @Autowired
    private SymmetricKeyJWTValidatorCacheService symmetricCacheService;

    @Autowired
    private CHUserDetailsService chUserDetailsService;

    /**
     * This method is a copy of org.mitre.openid.connect.service.impl.DefaultOIDCTokenService.createIdToken() with
     * modifications to add a group claim to the ID token.
     */
    @Override
    public JWT createIdToken(ClientDetailsEntity client, OAuth2Request request, Date issueTime, String sub, OAuth2AccessTokenEntity accessToken) {
        JWSAlgorithm signingAlg = jwtService.getDefaultSigningAlgorithm();

        if (client.getIdTokenSignedResponseAlg() != null) {
            signingAlg = client.getIdTokenSignedResponseAlg();
        }

        JWT idToken = null;
        JWTClaimsSet.Builder idClaims = new JWTClaimsSet.Builder();

        // ----- BEGIN Add ldap_groups claim
        Set<String> ldapGroups = chUserDetailsService.loadUserBySubject(sub).getLdapGroups();
        idClaims.claim("ldap_groups", ldapGroups);
        // ----- END Add ldap_groups claim

        // if the auth time claim was explicitly requested OR if the client always wants the auth time, put it in
        if (request.getExtensions().containsKey(MAX_AGE)
                || (request.getExtensions().containsKey("idtoken")) // TODO: parse the ID Token claims (#473) -- for now assume it could be in there
                || (client.getRequireAuthTime() != null && client.getRequireAuthTime())) {
            if (request.getExtensions().get(AuthenticationTimeStamper.AUTH_TIMESTAMP) != null) {
                Long authTimestamp = Long.parseLong((String) request.getExtensions().get(AuthenticationTimeStamper.AUTH_TIMESTAMP));
                if (authTimestamp != null) {
                    idClaims.claim("auth_time", authTimestamp / 1000L);
                }
            } else {
                // we couldn't find the timestamp!
                logger.warn("Unable to find authentication timestamp! There is likely something wrong with the configuration.");
            }
        }

        idClaims.issueTime(issueTime);

        if (client.getIdTokenValiditySeconds() != null) {
            Date expiration = new Date(System.currentTimeMillis() + (client.getIdTokenValiditySeconds() * 1000L));
            idClaims.expirationTime(expiration);
        }

        idClaims.issuer(configBean.getIssuer());
        idClaims.subject(sub);
        idClaims.audience(Lists.newArrayList(client.getClientId()));
        idClaims.jwtID(UUID.randomUUID().toString()); // set a random NONCE in the middle of it

        String nonce = (String) request.getExtensions().get(NONCE);
        if (!Strings.isNullOrEmpty(nonce)) {
            idClaims.claim("nonce", nonce);
        }

        Set<String> responseTypes = request.getResponseTypes();

        if (responseTypes.contains("token")) {
            // calculate the token hash
            Base64URL at_hash = IdTokenHashUtils.getAccessTokenHash(signingAlg, accessToken);
            idClaims.claim("at_hash", at_hash);
        }

        if (client.getIdTokenEncryptedResponseAlg() != null && !client.getIdTokenEncryptedResponseAlg().equals(Algorithm.NONE)
                && client.getIdTokenEncryptedResponseEnc() != null && !client.getIdTokenEncryptedResponseEnc().equals(Algorithm.NONE)
                && (!Strings.isNullOrEmpty(client.getJwksUri()) || client.getJwks() != null)) {

            JWTEncryptionAndDecryptionService encrypter = encrypters.getEncrypter(client);

            if (encrypter != null) {

                idToken = new EncryptedJWT(new JWEHeader(client.getIdTokenEncryptedResponseAlg(), client.getIdTokenEncryptedResponseEnc()), idClaims.build());

                encrypter.encryptJwt((JWEObject) idToken);

            } else {
                logger.error("Couldn't find encrypter for client: " + client.getClientId());
            }

        } else {

            if (signingAlg.equals(Algorithm.NONE)) {
                // unsigned ID token
                idToken = new PlainJWT(idClaims.build());

            } else {

                // signed ID token

                if (signingAlg.equals(JWSAlgorithm.HS256)
                        || signingAlg.equals(JWSAlgorithm.HS384)
                        || signingAlg.equals(JWSAlgorithm.HS512)) {

                    JWSHeader header = new JWSHeader(signingAlg, null, null, null, null, null, null, null, null, null,
                            jwtService.getDefaultSignerKeyId(),
                            null, null);
                    idToken = new SignedJWT(header, idClaims.build());

                    JWTSigningAndValidationService signer = symmetricCacheService.getSymmetricValidtor(client);

                    // sign it with the client's secret
                    signer.signJwt((SignedJWT) idToken);
                } else {
                    idClaims.claim("kid", jwtService.getDefaultSignerKeyId());

                    JWSHeader header = new JWSHeader(signingAlg, null, null, null, null, null, null, null, null, null,
                            jwtService.getDefaultSignerKeyId(),
                            null, null);

                    idToken = new SignedJWT(header, idClaims.build());

                    // sign it with the server's key
                    jwtService.signJwt((SignedJWT) idToken);
                }
            }

        }

        return idToken;
    }
}
