package samltest;

import org.springframework.security.core.Authentication;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping("/")
    @ResponseBody
    public String root(Authentication authentication) {
        return authentication.getName() + " _ " + ((SAMLCredential) authentication.getCredentials()).getAttributeAsString("tudStudentNumber");
    }

}
