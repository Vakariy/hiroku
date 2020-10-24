package com.hackathon.team5.controller;

import com.hackathon.team5.entity.CustomUser;
import com.hackathon.team5.enums.Role;
import com.hackathon.team5.service.SecurityService;
import com.hackathon.team5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityService securityService;

    @RequestMapping("/sign_in")
    String showSign_inPage() {
        return "sign_in";
    }


    @RequestMapping("/login")
    public String loginPage() {
        return "sign_in";
    }


    @RequestMapping("/change_password")
    String changePassw() {
        return "change_password";
    }

    @RequestMapping("/errorPage")
    String errorUrl() {
        return "error";
    }


    @RequestMapping("/logout")
    String logout() {
        return "login";
    }

    @RequestMapping("/sign_in?error=user-exist")
    String userExist() {
        return "sign_in";
    }




    @RequestMapping("/registration")
    ModelAndView registration(@RequestParam("fullname") String fullname,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password) {


        ModelAndView modelAndView = new ModelAndView("sign_in");

        String uuid = UUID.randomUUID().toString();
        String[] pib = new String[3];

        pib = fullname.split(" ");


        String passHash = passwordEncoder.encode(password);

        CustomUser customUser = new CustomUser(pib[0], pib[1], pib[2], email, passHash, Role.USER);
        customUser.setUuid(uuid);
        if (!userService.saveUser(customUser)) {
            modelAndView.addObject("message", "Error!! User with typed email exist!");
            return modelAndView;
        } else {
            securityService.autoLogin(email, password);
            // modelAndView.addObject("message", "Sign in to continue");
            return new ModelAndView("index");
        }
    }


    @RequestMapping("/send_mail")
    ModelAndView getPassword(@RequestParam String email) {

        ModelAndView modelAndView = new ModelAndView("sign_in");
        String message = "";

        if (!userService.sendUrlToChangePassword(email)) {
            message = "User with this email not exist!";

        } else message = "Check your email to change password!!";

        modelAndView.addObject("message", message);

        return modelAndView;
    }

    @RequestMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code, HttpServletRequest request) {

        CustomUser customUser = userService.findByUuid(code);

        if (customUser != null) {
            request.getSession().setAttribute("user", customUser);
            return "redirect:/change_password";
        } else return "errorPage";
    }


    @RequestMapping("/change")
    public ModelAndView changePassword(@RequestParam("password") String password,
                                       HttpServletRequest request
    ) {

        ModelAndView modelAndView = new ModelAndView("sign_in");
        ModelAndView fail = new ModelAndView("errorPage");
        if (userService.changePassword((CustomUser) request.getSession().getAttribute("user"), password)) {
            modelAndView.addObject("message", "Your password was changed");
            return modelAndView;
        } else return fail;


    }

    private String getEmailCurrentUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser.getName();


        return email;
    }
}
