package samltest;

import ch.wisv.dienst2.apiclient.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ModelAndView login(HttpSession session, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes
                .AUTHENTICATION_EXCEPTION);
        boolean showCHMessage = false;
        if (exception != null) {
            model.addAttribute("exceptionMessage", exception.getMessage());
            //session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception instanceof CHUserDetailsService.CHAuthenticationException) {
                showCHMessage = true;
            }
        }
        model.addAttribute("showCHMessage", showCHMessage);

        return new ModelAndView("login", model);
    }

    @RequestMapping("/chmembererror")
    public String chMemberError() {
        // We do not get exception when using ExceptionMappingAuthenticationFailureHandler.
        return "CH member not found";
    }

}
