package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.User;
import cz.uhk.sigmamail.model.UserDAO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class MainpageController {

    UserDAO userDAO;

    public MainpageController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof CustomUserDetails){
            CustomUserDetails loggedUser = (CustomUserDetails) principal;
            User user = userDAO.getUserByUserame(loggedUser.getUsername());
            model.addAttribute("user", user);
        }
        return "index";
    }
}
