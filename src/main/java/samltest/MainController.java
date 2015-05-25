package samltest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.opensaml.saml2.core.Attribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @RequestMapping("/")
    public String root(Authentication authentication) {
        return "index";
    }

    @RequestMapping("/protected")
    @ResponseBody
    public String protectedpage(Authentication authentication) throws JsonProcessingException {
        Object credentials = authentication.getCredentials();
        if (credentials != null && credentials instanceof SAMLCredential) {
            SAMLCredential samlCredential = (SAMLCredential) credentials;
            List<Attribute> attributes = samlCredential.getAttributes();
            String attributesString = attributes.stream().map(Attribute::getName).map(n -> n + ": " + samlCredential
                    .getAttributeAsString(n)).collect(Collectors.joining(",\n"));
            return "<pre>Authenticated\n\n" + authentication.getName() + "\n\n" + attributesString;
        } else {
            return "<pre>Authenticated\n\n" + authentication.getName();
        }
    }

    // Login form
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

}
