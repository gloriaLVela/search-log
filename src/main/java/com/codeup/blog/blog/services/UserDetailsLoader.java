package com.codeup.blog.blog.services;

import com.codeup.blog.blog.models.User;
import com.codeup.blog.blog.models.UserWithRoles;
import com.codeup.blog.blog.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsLoader implements UserDetailsService {
    private final UserRepository users;

    public UserDetailsLoader(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        User user = users.findByUsername(username);
        System.out.println("user = " + user);
        if (user == null) {
            throw new UsernameNotFoundException("No user found for " + username);
        }

        return new UserWithRoles(user);
    }
}