package cz.uhk.sigmamail.web;

import cz.uhk.sigmamail.user.CustomUserDetails;
import cz.uhk.sigmamail.model.User;
import cz.uhk.sigmamail.model.UserDAO;
import cz.uhk.sigmamail.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RegistrationController {

    @Autowired
    private final UserRepository userRepository;

    private final UserDAO userDAO;

    public RegistrationController(UserRepository userRepository, UserDAO userDAO){
        this.userRepository = userRepository;
        this.userDAO = userDAO;
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

    @GetMapping("/register")
    public String getRegistrationPage(Model model){
        User user = getLoggedUser();
        model.addAttribute("user",user);
        return "register";
    }


    @PostMapping("/register/save")
    public String registerUser(HttpServletRequest request, Model model){
        User user = getLoggedUser();
        model.addAttribute("user",user);

        String minLengthRegex = ".{8,}";
        String capitalLetterRegex = "[A-Z]";
        String numberRegex = "\\d";

        int errorValue;
        String username = request.getParameter("username");
        if (userDAO.getUserByUserame(username) != null)
        {
            errorValue = 5;
            model.addAttribute("errorValue", errorValue);
            return "register";

        }
        if(!username.matches("^[a-zA-Z0-9.]+$")) {
            errorValue = 1;
            model.addAttribute("errorValue", errorValue);
            return "register";
        }
        username = username + "@sigma.com";
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        if(!first_name.matches("^[a-zA-Z]+$") || !last_name.matches("^[a-zA-Z]+$"))
        {
            errorValue = 4;
            model.addAttribute("errorValue", errorValue);
            return "register";
        }
        String birthdateString = request.getParameter("birthdate");
        if(birthdateString.isEmpty()){
            errorValue = 6;
            model.addAttribute("errorValue", errorValue);
            return "register";
        }
        String password = request.getParameter("password");
        if (!password.matches(minLengthRegex) || !password.matches(".*" + capitalLetterRegex + ".*") || !password.matches(".*" + numberRegex + ".*")) {
            errorValue = 2;
            model.addAttribute("errorValue", errorValue);
            return "register";
        }
        String password_again = request.getParameter("password_again");
        if(!password_again.equals(password)){
            errorValue = 3;
            model.addAttribute("errorValue", errorValue);
            return "register";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        User newUser = new User();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date birthdate = simpleDateFormat.parse(birthdateString);
            System.out.println("date: " + birthdate);
            newUser.setBirthdate(birthdate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Password: " + encodedPassword);
        newUser.setUsername(username);
        newUser.setFirst_name(first_name);
        newUser.setLast_name(last_name);
        newUser.setPassword(encodedPassword);
        newUser.setRole("User");

        userRepository.save(newUser);

        return "register-successful";
    }
}
