package com.codeup.blog.blog.controllers;

import com.codeup.blog.blog.models.Company;
import com.codeup.blog.blog.models.JobPost;
import com.codeup.blog.blog.models.User;
import com.codeup.blog.blog.repositories.CompanyRepository;
import com.codeup.blog.blog.repositories.PostRepository;
import com.codeup.blog.blog.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CompanyController {

    private final CompanyRepository companyDao;
    private final UserRepository userDao;
    private final PostRepository postDao;

    public CompanyController(CompanyRepository companyDao, UserRepository userDao, PostRepository postDao) {
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.postDao = postDao;
    }

    @GetMapping("/companies")
    public String index(Model viewModel) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Company> companyList = userDao.getOne(loggedUser.getId()).getCompanyList();
//        viewModel.addAttribute("user", jobPosts.);
        viewModel.addAttribute("companies", companyList);
        return "company/maintain-company";
    }

    @GetMapping("/company/{id}")
    public String show(@PathVariable long id, Model viewModel) {
        System.out.println("id = " + id);
        viewModel.addAttribute("company", companyDao.getOne(id));
        viewModel.addAttribute("newLineChar", '\n');
        return "company/show-company";
    }

    @GetMapping("/company/add")
    public String showCreatePost(Model vModel) {
        vModel.addAttribute("company", new Company());
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        vModel.addAttribute("userId", loggedUser.getId());
        return "/company/add-company";
    }

    @PostMapping("/company/add")
    public String create(@ModelAttribute Company newCompany,
                         Model vModel) {
        System.out.println("create");
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newCompany.setUser(loggedUser);
        companyDao.save(newCompany);
        return "redirect:/companies";
    }


    @GetMapping("/company/{id}/update")
    public String updatePost(@PathVariable long id, Model viewModel) {
        int index;
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewModel.addAttribute("company", companyDao.getOne(id));

        return "/company/update-company";

    }

    @PostMapping("/company/{id}/update")
    public String update(@PathVariable long id,
                         @RequestParam String name,
                         @RequestParam String website,
                         @RequestParam String information,
                         @RequestParam String news) {
        Company oldCompany = companyDao.getOne(id);
        oldCompany.setName(name);
        oldCompany.setWebsite(website);
        oldCompany.setInformation(information);
        oldCompany.setNews(news);
        companyDao.save(oldCompany);
        return "redirect:/companies";
    }

    @PostMapping("/company/{id}/delete")
    public String delete(@PathVariable long id) {
        Company currentCompany = companyDao.getOne(id);
        List<JobPost> jobPostList = postDao.findAllByCompanyEquals(id);
        while (!jobPostList.isEmpty()){
            postDao.delete(jobPostList.get(0));
        }
        companyDao.deleteById(id);
        return "redirect:/companies";
    }


}
