package ch.wisv.connect.overlay.services;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.mitre.openid.connect.service.ScopeClaimTranslationService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Scope claim translation service.
 * <p>
 * Service to map scopes to claims, and claims to Java field names. Replaces MITREid Connect's
 * DefaultScopeClaimTranslationService.
 */
@Service("scopeClaimTranslator")
public class CHScopeClaimTranslationService implements ScopeClaimTranslationService {

    private SetMultimap<String, String> scopesToClaims = HashMultimap.create();

    /**
     * Default constructor; initializes scopesToClaims map
     */
    public CHScopeClaimTranslationService() {
        scopesToClaims.put("openid", "sub");

        scopesToClaims.put("profile", "name");
        scopesToClaims.put("profile", "preferred_username");
        scopesToClaims.put("profile", "given_name");
        scopesToClaims.put("profile", "family_name");
        scopesToClaims.put("profile", "middle_name");
        scopesToClaims.put("profile", "nickname");
        scopesToClaims.put("profile", "profile");
        scopesToClaims.put("profile", "picture");
        scopesToClaims.put("profile", "website");
        scopesToClaims.put("profile", "gender");
        scopesToClaims.put("profile", "zone_info");
        scopesToClaims.put("profile", "locale");
        scopesToClaims.put("profile", "updated_at");
        scopesToClaims.put("profile", "birthdate");

        scopesToClaims.put("email", "email");
        scopesToClaims.put("email", "email_verified");

        scopesToClaims.put("phone", "phone_number");
        scopesToClaims.put("phone", "phone_number_verified");

        scopesToClaims.put("address", "address");

        scopesToClaims.put("ldap", "ldap_username");
        scopesToClaims.put("ldap", "ldap_groups");

        scopesToClaims.put("student", "netid");
        scopesToClaims.put("student", "student_number");

    }

    @Override
    public Set<String> getClaimsForScope(String scope) {
        if (scopesToClaims.containsKey(scope)) {
            return scopesToClaims.get(scope);
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public Set<String> getClaimsForScopeSet(Set<String> scopes) {
        Set<String> result = new HashSet<>();
        for (String scope : scopes) {
            result.addAll(getClaimsForScope(scope));
        }
        return result;
    }

    @Override
    public String getFieldNameForClaim(String claim) {
        throw new UnsupportedOperationException();
    }

}
