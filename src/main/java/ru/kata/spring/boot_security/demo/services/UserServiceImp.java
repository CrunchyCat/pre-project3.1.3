package ru.kata.spring.boot_security.demo.services;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;


import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UsersRepository usersRepository;
    @Autowired
    public UserServiceImp(UsersRepository usersRepository) {
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
    @Override
    @Transactional
    public void save(User user) {
        usersRepository.save(user);
    }
    @Override
    @Transactional
    public void update(int id, User updateUser) {
        updateUser.setId(id);
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
