package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepo;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;
@Service
public class UserDetailsServiceAuthentication implements UserDetailsService {
    private UsersRepo usersRepo;
    @Autowired
    public UserDetailsServiceAuthentication(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public User findByEmail(String email) {
        return usersRepo.findByEmail(email);
    }

    private Collection<?extends GrantedAuthority> mapRolesToAthorities(Collection<Role> roles) {
        return roles.stream().map(role ->
                        new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        if (user == null) {
            throw  new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAthorities(user.getRoles())
        );
    }

}
