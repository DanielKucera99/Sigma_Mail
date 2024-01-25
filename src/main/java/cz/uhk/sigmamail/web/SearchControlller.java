package cz.uhk.sigmamail.web;

import cz.uhk.sigmamail.user.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import cz.uhk.sigmamail.model.User;
import cz.uhk.sigmamail.model.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SearchControlller {

    private final UserDAO userDAO;

    public SearchControlller(UserDAO userDAO){
        this.userDAO=userDAO;
    }
    public User getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user = new User();
        if(principal instanceof CustomUserDetails){
            CustomUserDetails loggedUser = (CustomUserDetails) principal;
            user = userDAO.getUserByUserame(loggedUser.getUsername());
        }
        return user;
    }
    @PostMapping("/search/{username}")
    public String getSearchPage(@PathVariable String username, Model model){
        User user = getLoggedUser();
        List<User> foundUsers = userDAO.getUsersByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("foundUsers", foundUsers);

        return "search";
    }
}
