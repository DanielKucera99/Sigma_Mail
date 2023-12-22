package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.User;
import org.springframework.ui.Model;
import cz.uhk.sigmamail.model.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    private final UserDAO userDAO;

    public ProfileController(UserDAO userDAO){
        this.userDAO=userDAO;
    }

    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable int userId, Model model){
        int userid = 1;
        User user = userDAO.getUserById(userid);
        User profile_owner = userDAO.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("profile_owner", profile_owner);

        return "profile";
    }
}
