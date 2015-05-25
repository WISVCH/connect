package samltest;

import ch.wisv.dienst2.apiclient.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
public class MainController {
    @RequestMapping("/")
    public String root(Authentication authentication) {
        return "index";
    }

    @RequestMapping("/protected")
    @ResponseBody
    public String protectedpage(Authentication authentication) throws JsonProcessingException {
        if (authentication instanceof CHAuthenticationToken) {
            CHUserDetails userDetails = (CHUserDetails) authentication.getPrincipal();
            Person person = userDetails.getPerson();
            Set<String> ldapGroups = userDetails.getLdapGroups();
            return String.format("<pre>Authenticated\n\n%s\n\n%s\nLDAP Groups: %s", authentication.getName(), person
                    .getFormattedName(), ldapGroups);
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
