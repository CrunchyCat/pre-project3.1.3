package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UsersRepository usersRepository;
    @Autowired
    public UserServiceImp(PasswordEncoder passwordEncoder, RoleService roleService, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.usersRepository = usersRepository;
    }
    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }


    @Override
    public User findOne(int id) {
        Optional<User> foundUser= usersRepository.findById(id);
        return foundUser.orElse(null);
    }


    //Метод для получения юзера из его сессии после аутентификации
    @Override
    public User getUserFromPrincipal(Principal principal) {
        User user = usersRepository.findByUsername(principal.getName()).get();
        return findOne(user.getId());
    }



    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }
    @Override
    @Transactional
    public void update(int id, User updateUser) {
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        updateUser.setRoles(roleService.findOne(1));
        usersRepository.save(updateUser);
    }
    @Override
    @Transactional
    public void delete(int id) {
        User user = findOne(id);
        user.getRoles().clear();
        usersRepository.save(user);
        usersRepository.deleteById(id);
    }
}
