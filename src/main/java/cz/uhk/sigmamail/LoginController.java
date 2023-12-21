package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }
}
