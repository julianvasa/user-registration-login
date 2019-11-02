package com.staxter.web;

import com.staxter.exception.UserAlreadyExistsException;
import com.staxter.model.User;
import com.staxter.model.UserAlreadyExists;
import com.staxter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * Main controller to handle post requests for user registration
 *
 * @author Julian Vasa
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${username_already_exists.code}")
    private String USER_ALREADY_EXISTS;
    @Value("${username_already_exists.description}")
    private String A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS;

    /* Post endpoint to create a new user, accepts a valid JSON User in the RequestBody */
    @PostMapping("/userservice/register")
    public ResponseEntity registration(@Valid @RequestBody User userForm) {
        try {
            /* If no exception is thrown return the user created with http status 200 */
            User userCreated = userService.createUser(userForm);
            return new ResponseEntity<>(userCreated, HttpStatus.OK);
        } catch (UserAlreadyExistsException exception) {
            /* If exception is thrown return an object of type UserAlreadyExists(code, descrption) with http status 409 */
            UserAlreadyExists userAlreadyExistsError = UserAlreadyExists.builder().code(USER_ALREADY_EXISTS).description(A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS).build();
            return new ResponseEntity<>(userAlreadyExistsError, HttpStatus.CONFLICT);
        }
    }

    /* User login form, return login.html template */
    @GetMapping("/login")
    public String getLogin(Model model) {
        return "login";
    }

    /* Show an error in the login template if user & password are not correct or the user does not exists */
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    /* Create a new user through the frontend */
    @PostMapping("/signup")
    public String signup(Model model, @ModelAttribute User user) {
        try {
            /* If no exception is thrown return the user created with http status 200 */
            User userCreated = userService.createUser(user);
            model.addAttribute("signup_done", true);
            return "login";
        } catch (UserAlreadyExistsException exception) {
            /* If exception is thrown return an object of type UserAlreadyExists(code, descrption) with http status 409 */
            UserAlreadyExists userAlreadyExistsError = UserAlreadyExists.builder().code(USER_ALREADY_EXISTS).description(A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS).build();
            return "redirect:/denied";
        }
    }

    /* On successful logout redirect to login page */
    @GetMapping("/logoutSuccessful")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return "login";
    }

    /* Get the welcome template if user has been logged in */
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
}
