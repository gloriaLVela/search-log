package com.codeup.blog.blog.controllers;

import com.codeup.blog.blog.models.Company;
import com.codeup.blog.blog.models.JobPost;
import com.codeup.blog.blog.models.User;
import com.codeup.blog.blog.repositories.CompanyRepository;
import com.codeup.blog.blog.repositories.PostRepository;
import com.codeup.blog.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
//        vModel.addAttribute("applied_date", LocalDate.now());
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        vModel.addAttribute("userId", loggedUser.getId());
        vModel.addAttribute("companies", userDao.getOne(loggedUser.getId()).getCompanyList());
        return "/posts/create";
    }

    @PostMapping("/posts/create")
    public String create(@ModelAttribute JobPost newJobPost,
                         @RequestParam Long company_id,
                         @RequestParam String strApplied_date,
                         @RequestParam String strNotified_date,
                         @RequestParam(name = "coverURL",
                                 required = false) String coverURL,
                         @RequestParam(name = "resumeURL",
                                 required = false) String resumeURL,
                         Model vModel) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newJobPost.setUser(loggedUser);
        newJobPost.setCompany(companyDao.getOne(company_id));
        Date applied_date = null;
        try {
            applied_date = new SimpleDateFormat("yyyy-mm-dd").parse(strApplied_date);
            newJobPost.setApplied_date(applied_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date notified_date = null;
        try {
            notified_date = new SimpleDateFormat("yyyy-mm-dd").parse(strNotified_date);
            newJobPost.setNotified_date(notified_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newJobPost.setCover_url(coverURL);
        newJobPost.setResume_url(resumeURL);
        newJobPost.setTime_stamp(new Date());
        newJobPost.setActive(true);
        postDao.save(newJobPost);
        return "redirect:/myPosts";
    }


    @GetMapping("/posts/{id}/update")
    public String updatePost(@PathVariable long id, Model viewModel) {
        viewModel.addAttribute("post", postDao.getOne(id));
        viewModel.addAttribute("selectedCompany", postDao.getOne(id).getCompany());
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        viewModel.addAttribute("userId", loggedUser.getId());
        viewModel.addAttribute("companies", userDao.getOne(loggedUser.getId()).getCompanyList());
        return "posts/update";

    }

    @PostMapping("/posts/{id}/update")
    public String update(@PathVariable long id,
                         @RequestParam String title,
                         @RequestParam String description,
                         @RequestParam Long company_id,
                         @RequestParam String applied_date,
                         @RequestParam String interview_date,
                         @RequestParam String notified_date,
                         @RequestParam String thank_you_sent,
                         @RequestParam String check_status,
                         @RequestParam(name = "interview_attendants", required = false) String interview_attendants,
                         @RequestParam String notes,
                         @RequestParam String timeline,
                         @RequestParam(name = "coverURL",
                                 required = false) String coverURL,
                         @RequestParam(name = "resumeURL",
                                 required = false) String resumeURL
    ) {

        JobPost oldJobPost = postDao.getOne(id);
        oldJobPost.setTitle(title);
        oldJobPost.setDescription(description);
        oldJobPost.setCompany(companyDao.getOne(company_id));
        Date applied_date2 = null;
        try {
            applied_date2 = new SimpleDateFormat("yyyy-mm-dd").parse(applied_date);
            oldJobPost.setApplied_date(applied_date2);
        } catch (ParseException e) {
            System.out.println("exception 1");
            e.printStackTrace();
        }

        Date interview_date2 = null;
        try {
            interview_date2 = new SimpleDateFormat("yyyy-mm-dd").parse(interview_date);
            oldJobPost.setInterview_date(interview_date2);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("exception 2");
        }

        Date notified_date2 = null;
        try {
            notified_date2 = new SimpleDateFormat("yyyy-mm-dd").parse(notified_date);
            oldJobPost.setNotified_date(notified_date2);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("exception 3");
        }

        Date thank_you_sent2 = null;
        try {
            thank_you_sent2 = new SimpleDateFormat("yyyy-mm-dd").parse(thank_you_sent);
            oldJobPost.setThank_you_sent(thank_you_sent2);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("exception 4");
        }

        Date check_status2 = null;
        try {
            check_status2 = new SimpleDateFormat("yyyy-mm-dd").parse(check_status);
            oldJobPost.setCheck_status(check_status2);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("exception 5");
        }

        oldJobPost.setInterview_attendants(interview_attendants);
        oldJobPost.setNotes(notes);
        oldJobPost.setTimeline(timeline);
        oldJobPost.setCover_url(coverURL);
        oldJobPost.setResume_url(resumeURL);
        oldJobPost.setActive(true);
        postDao.save(oldJobPost);
        return "redirect:/myPosts";
    }

    @PostMapping("/posts/{id}/delete")
    public String delete(@PathVariable long id) {
        JobPost currentJobPost = postDao.getOne(id);
        currentJobPost.setActive(false);
        postDao.save(currentJobPost);
        //postDao.deleteById(id);
        return "redirect:/myPosts";
    }


}
