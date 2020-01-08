package com.codeup.blog.blog.controllers;

import com.codeup.blog.blog.models.JobPost;
import com.codeup.blog.blog.models.User;
import com.codeup.blog.blog.repositories.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class UserController {

    //private final UserRepository users;

    private PasswordEncoder passwordEncoder;


    private final UserRepository userDao;


    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerUser(Model viewModel) {
        viewModel.addAttribute("user", new User());
        return "/sign-up";
    }


    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User newUser) {
        String hash = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hash);
        userDao.save(newUser);
        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String logoutUser() {
        return "redirect:/home";
    }

    @GetMapping("/myPosts")
    public String displayProfile(Model viewModel) {
//
        System.out.println("myPosts");

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(loggedUser.toString());
        List<JobPost> jobPosts = userDao.getOne(loggedUser.getId()).getJobPosts();
        for (int index = jobPosts.size() - 1; index >= 0; index--) {
            if (jobPosts.get(index).isActive() == false) {
                jobPosts.remove(index);

            }
        }
        viewModel.addAttribute("user", userDao.getOne(loggedUser.getId()));
        viewModel.addAttribute("posts", jobPosts);
        viewModel.addAttribute("newLineChar", '\n');
        viewModel.addAttribute("totalPost", jobPosts.size());
        return "users/profile";
    }

}