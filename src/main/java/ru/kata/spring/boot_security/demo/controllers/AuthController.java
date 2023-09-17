package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final RoleService roleService;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator, RoleRepository roleRepository, RoleService roleService) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        System.out.println("Inside 'AuthController.loginPage()'");
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        System.out.println("Inside 'AuthController.registrationPage()'");
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService);
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      @RequestParam("selectedRole") String selectedRole,
                                      BindingResult bindingResult, Model model) {
        System.out.println("Inside 'AuthController.performRegistration()'");
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("Inside 'AuthController.performRegistration() HAS ERRORS'");
            return "/auth/registration";
        }

        System.out.println("selectRole - " + selectedRole);
        registrationService.register(user, selectedRole);
        System.out.println("Inside 'AuthController.performRegistration() after registration'");

        return "redirect:/";
    }
}
