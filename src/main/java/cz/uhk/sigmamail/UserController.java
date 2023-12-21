package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.UserDAO;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private UserDAO userDAO;

    @GetMapping("/user")
    public String getUsers(Model model) {
        var users = userDAO.getAllUsers();
        model.addAttribute("users", users);

        return "users";
    }
}
