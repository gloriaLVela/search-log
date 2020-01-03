package com.codeup.blog.blog.controllers;
import com.codeup.blog.blog.models.Company;
import com.codeup.blog.blog.models.JobPost;
import com.codeup.blog.blog.models.User;
import com.codeup.blog.blog.repositories.CompanyRepository;
import com.codeup.blog.blog.repositories.PostRepository;
import com.codeup.blog.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    private final PostRepository postDao;
    private final UserRepository userDao;
    private final CompanyRepository companyDao;

    public PostController(PostRepository postDao,
                          UserRepository userDao,
                          CompanyRepository companyDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.companyDao = companyDao;
    }

    @GetMapping("/home")
    public String home(Model viewModel) {
        return "home";

    }


    @GetMapping("/showBlog/{id}")
    public String listPosts(@PathVariable long id, Model viewModel) {
        User selectedBlog = userDao.getOne(id);
        List<JobPost> jobPosts = selectedBlog.getJobPosts();
        viewModel.addAttribute("user", selectedBlog);
        viewModel.addAttribute("posts", jobPosts);
        return "posts/index";
    }

    @GetMapping("/posts")
    public String index(Model viewModel) {
        List<JobPost> jobPosts = postDao.findAll();
//        viewModel.addAttribute("user", jobPosts.);
        viewModel.addAttribute("posts", jobPosts);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String show(@PathVariable long id, Model viewModel) {
        System.out.println("id = " + id);
        viewModel.addAttribute("post", postDao.getOne(id));
        viewModel.addAttribute("newLineChar", '\n');
        return "posts/show";
    }

    @GetMapping("/posts/create")
    public String showCreatePost(Model vModel) {
        vModel.addAttribute("post", new JobPost());
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        vModel.addAttribute("userId", loggedUser.getId());
        vModel.addAttribute("companies", userDao.getOne(loggedUser.getId()).getCompanyList());
        return "/posts/create";
    }

    @PostMapping("/posts/create")
    public String create(@ModelAttribute JobPost newJobPost,
                         @RequestParam Long company_id,
                         @RequestParam Date applied_date,
                         @RequestParam Date notified_date,
                         Model vModel) {
        System.out.println("create");
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newJobPost.setUser(loggedUser);
        newJobPost.setCompany(companyDao.getOne(company_id));
        newJobPost.setApplied_date(applied_date);
        newJobPost.setNotified_date(notified_date);
        postDao.save(newJobPost);
        return "redirect:/myPosts";
    }


    @GetMapping("/posts/{id}/update")
    public String updatePost(@PathVariable long id, Model viewModel) {
         viewModel.addAttribute("post", postDao.getOne(id));
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewModel.addAttribute("userId", loggedUser.getId());
        viewModel.addAttribute("companies", userDao.getOne(loggedUser.getId()).getCompanyList());
        return "posts/update";

    }

    @PostMapping("/posts/{id}/update")
    public String update(@PathVariable long id,
                         @RequestParam String title,
                         @RequestParam String description) {
        System.out.println("update post");
        JobPost oldJobPost = postDao.getOne(id);
        oldJobPost.setTitle(title);
        oldJobPost.setDescription(description);
        postDao.save(oldJobPost);
        return "redirect:/myPosts";
    }

    @PostMapping("/posts/{id}/delete")
    public String delete(@PathVariable long id) {
        JobPost currentJobPost = postDao.getOne(id);
        postDao.deleteById(id);
        return "redirect:/myPosts";
    }


}
