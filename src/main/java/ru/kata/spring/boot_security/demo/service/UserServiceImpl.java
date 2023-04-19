package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepo;
import ru.kata.spring.boot_security.demo.repositories.UsersRepo;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private UsersRepo usersRepo;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;
@Autowired
    public UserServiceImpl(UsersRepo usersRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return usersRepo.findAll();
    }
    @Override
    public User findOne(int id) {
        Optional<User> foundUser = usersRepo.findById(id);
        return foundUser.orElse(null);
    }
    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepo.findById(2).orElse(null));
        usersRepo.save(user);
    }
    @Override
    @Transactional
    public void update(int id, User updateUser) {
        updateUser.setId(id);
        usersRepo.save(updateUser);
    }
    @Override
    @Transactional
    public void delete(int id) {
        User user = findOne(id);
        user.getAuthorities().clear();
        usersRepo.save(user);
        usersRepo.deleteById(id);
    }
}
