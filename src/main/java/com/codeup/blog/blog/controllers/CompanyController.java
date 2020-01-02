package com.codeup.blog.blog.controllers;

import com.codeup.blog.blog.models.Company;
import com.codeup.blog.blog.models.JobPost;
import com.codeup.blog.blog.models.User;
import com.codeup.blog.blog.repositories.CompanyRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CompanyController {

    private final CompanyRepository companyDao;

    public CompanyController(CompanyRepository companyDao) {
        this.companyDao = companyDao;
    }

    @GetMapping("/companies")
    public String index(Model viewModel) {
        List<Company> companyList = companyDao.findAll();
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
                         @RequestParam String information,
                         @RequestParam String news) {
        Company oldCompany = companyDao.getOne(id);
        oldCompany.setName(name);
        oldCompany.setInformation(information);
        oldCompany.setNews(news);
        companyDao.save(oldCompany);
        return "redirect:/companies";
    }

    @PostMapping("/company/{id}/delete")
    public String delete(@PathVariable long id) {
        Company currentCompany = companyDao.getOne(id);
        companyDao.deleteById(id);
        return "redirect:/companies";
    }


}
