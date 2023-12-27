package cz.uhk.sigmamail.web;

import cz.uhk.sigmamail.user.CustomUserDetails;
import cz.uhk.sigmamail.model.MessageDAO;
import cz.uhk.sigmamail.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import cz.uhk.sigmamail.model.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ProfileController {

    private final UserDAO userDAO;
    private final MessageDAO messageDAO;

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

    public ProfileController(UserDAO userDAO, MessageDAO messageDAO){
        this.userDAO=userDAO;
        this.messageDAO=messageDAO;
    }

    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable int userId, Model model){
        User user = getLoggedUser();
        User profile_owner = userDAO.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("profile_owner", profile_owner);
        model.addAttribute("formattedBirthdate", new SimpleDateFormat("yyyy-MM-dd").format(profile_owner.getBirthdate()));

        return "profile";
    }

    @PostMapping("/profile/{userId}/update")
    public String getProfileUpdatePage(@PathVariable int userId, Model model){
        User user = userDAO.getUserById(userId);
        model.addAttribute("user",user);
        model.addAttribute("formattedBirthdate", new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthdate()));

        return "update-profile";
    }

    @PostMapping("/profile/{userId}/update/save")
    public String saveProfileUpdate(@PathVariable int userId, HttpServletRequest request, Model model){
        User user = userDAO.getUserById(userId);

        String username = request.getParameter("username");
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String birthdateString = request.getParameter("birthdate");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date birthdate = simpleDateFormat.parse(birthdateString);
            System.out.println("date: " + birthdate);
            user.setBirthdate(birthdate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        user.setUsername(username);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        userDAO.updateUser(user);

        model.addAttribute("user",user);
        model.addAttribute("profile_owner", user);
        model.addAttribute("formattedBirthdate", new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthdate()));

        return "profile";
    }

    @PostMapping("profile/{userId}/delete")
    public String deleteProfile(@PathVariable int userId, Model model){
        User user = userDAO.getUserById(userId);
        messageDAO.removeUser(user);
        userDAO.deleteUser(user);

        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("profile/{ownerId}/change-role/toAdmin")
    public String changeToAdmin(@PathVariable int ownerId, Model model){
        User user = getLoggedUser();
        User owner = userDAO.getUserById(ownerId);
        owner.setRole("Admin");
        userDAO.updateUser(owner);

        model.addAttribute("user",user);
        model.addAttribute("profile_owner", owner);
        model.addAttribute("formattedBirthdate", new SimpleDateFormat("yyyy-MM-dd").format(owner.getBirthdate()));

        return "profile";
    }
    @PostMapping("profile/{ownerId}/change-role/toUser")
    public String changeToUser(@PathVariable int ownerId, Model model){
        User user = getLoggedUser();
        User owner = userDAO.getUserById(ownerId);
        owner.setRole("User");
        userDAO.updateUser(owner);

        model.addAttribute("user",user);
        model.addAttribute("profile_owner", owner);
        model.addAttribute("formattedBirthdate", new SimpleDateFormat("yyyy-MM-dd").format(owner.getBirthdate()));

        return "profile";
    }

    @PostMapping("/profile/{userId}/change-password")
    public String getPasswordChangePage(@PathVariable int userId, Model model){
        User user = userDAO.getUserById(userId);
        model.addAttribute("user",user);

        return "change-password";
    }
    @PostMapping("/profile/{userId}/change-password/save")
    public String savePasswordChange(@PathVariable int userId, HttpServletRequest request, Model model){
        User user = userDAO.getUserById(userId);
        int errorValue;
        model.addAttribute("user",user);
        String minLengthRegex = ".{8,}";
        String capitalLetterRegex = "[A-Z]";
        String numberRegex = "\\d";
        String new_password = request.getParameter("new_password");

        if (!new_password.matches(minLengthRegex) || !new_password.matches(".*" + capitalLetterRegex + ".*") || !new_password.matches(".*" + numberRegex + ".*")) {
            errorValue = 2;
            model.addAttribute("errorValue", errorValue);
            return "change-password";
        }

        String confirm_new_password = request.getParameter("confirm_new_password");
        if(!confirm_new_password.equals(new_password)){
            errorValue = 3;
            model.addAttribute("errorValue", errorValue);
            return "change-password";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encoded_new_password = bCryptPasswordEncoder.encode(new_password);

        user.setPassword(encoded_new_password);
        userDAO.updateUser(user);


        model.addAttribute("profile_owner", user);
        model.addAttribute("formattedBirthdate", new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthdate()));

        return "profile";
    }

}
