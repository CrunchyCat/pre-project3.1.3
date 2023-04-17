package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UsersController {

    private final UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService;

    private final AuthController authController;

    private final RoleService roleService;


    @Autowired
    public UsersController(UsersRepository usersRepository, UserService userService, AuthController authController, RoleService roleService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
        this.authController = authController;
        this.roleService = roleService;
    }



    @GetMapping()
    public String show(Principal principal, Model model) {
        User user = usersRepository.findByUsername(principal.getName()).get();
        model.addAttribute("user", userService.findOne(user.getId()));
        return "users/show";
    }



    @GetMapping("/edit")
    public String edit(Model model, Principal principal) {
        User user = usersRepository.findByUsername(principal.getName()).get();

        model.addAttribute("user", userService.findOne(user.getId()));
        return "users/edit";
    }



    @PatchMapping()
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         Principal principal) {

        if(bindingResult.hasErrors()) {
            return "users/edit";
        }

        user.setRoles(roleService.findOne(1));
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User user1 = usersRepository.findByUsername(principal.getName()).get();
        userService.update(user1.getId(), user);
        return authController.loginPage();
    }


    @DeleteMapping()
    public String delete(Principal principal) {
        User user = usersRepository.findByUsername(principal.getName()).get();
        userService.delete(user.getId());
        return "redirect:/";
    }

}
