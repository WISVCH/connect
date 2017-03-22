package ch.wisv.connect.overlay.web;

import org.mitre.discovery.web.DiscoveryEndpoint;
import org.mitre.openid.connect.view.JsonEntityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.Map;

import static org.mitre.discovery.web.DiscoveryEndpoint.OPENID_CONFIGURATION_URL;

/**
 * Override OIDC discovery endpoint
 */
@Controller
public class DiscoveryEndpointOverride {

    @Autowired
    DiscoveryEndpoint discoveryEndpoint;

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(DiscoveryEndpointOverride.class);

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/" + OPENID_CONFIGURATION_URL, method = RequestMethod.GET)
    public String providerConfiguration(Model model) {
        String ret = discoveryEndpoint.providerConfiguration(model);
        Map<String, Object> entity = (Map<String, Object>) model.asMap().get(JsonEntityView.ENTITY);
        ((Collection) entity.get("token_endpoint_auth_methods_supported")).remove("none");
        return ret;
    }

}
