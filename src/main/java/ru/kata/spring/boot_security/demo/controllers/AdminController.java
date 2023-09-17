package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService,
                           UserValidator userValidator, RegistrationService registrationService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping
    public String showAllPeople(Model model) {
        System.out.println("Inside 'AdminController.showAllPeople()'");
        model.addAttribute("users", userService.getAllUsers());
        return "admin/admin";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        System.out.println("Inside 'AdminController.showUser()'");
        model.addAttribute("user", userService.getUser(id));
        return "admin/show";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        System.out.println("Inside 'AdminController.newUser()'");
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService);
        return "admin/new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             @RequestParam("selectedRole") String selectedRole,
                             BindingResult bindingResult) {
        System.out.println("Inside 'AdminController.createUser(): before validate'");
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("Inside 'AdminController.showAllPeople()': ERROR");
            return "redirect:/admin/new";
        }

        registrationService.register(user, selectedRole);
        System.out.println("Inside 'AdminController.createUser()': after register");

        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {
        System.out.println("Inside 'AdminController.editUser()'");
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        System.out.println(user);
        model.addAttribute("roles", roleService);
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id,
                             @RequestParam("selectedRole") String selectedRole) {
        System.out.println("Inside 'AdminController.updateUser()': before update");
        System.out.println(user);
        Role newRole = new Role(selectedRole);
        user.getRole().add(newRole);
        userService.updateUser(id, user);
        System.out.println("Inside 'AdminController.updateUser()': after update");
        System.out.println(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
