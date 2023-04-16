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
    private final UserService userService;


    @Autowired
    public UsersController(UsersRepository usersRepository, UserService userService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
    }



    @GetMapping()
    public String show(Principal principal, Model model) {
        User user = usersRepository.findByUsername(principal.getName()).get();
        model.addAttribute("user", userService.findOne(user.getId()));
        return "users/show";
    }



    @DeleteMapping()
    public String delete(Principal principal) {
        User user = usersRepository.findByUsername(principal.getName()).get();
        userService.delete(user.getId());
        return "redirect:/";
    }

}
