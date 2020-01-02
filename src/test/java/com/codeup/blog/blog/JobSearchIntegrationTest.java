package com.codeup.blog.blog;

import com.codeup.blog.blog.models.JobPost;
import com.codeup.blog.blog.models.User;
import com.codeup.blog.blog.repositories.PostRepository;
import com.codeup.blog.blog.repositories.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.hamcrest.core.StringContains.containsString;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.codeup.blog.blog.SearchLogApplication.class)
@AutoConfigureMockMvc
public class JobSearchIntegrationTest {
    private User testUser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    PostRepository postDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");

        // Creates the test user if not exists
        if (testUser == null) {
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword(passwordEncoder.encode("pass"));
            newUser.setEmail("testUser@codeup.com");
            System.out.println("newUser.toString() = " + newUser.toString());
            testUser = userDao.save(newUser);
        }

        // Throws a JobPost request to /login and expect a redirection to the posts index page after being logged in
        httpSession = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "pass"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/myPosts"))
                .andReturn()
                .getRequest()
                .getSession();
    }


    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        // It makes sure the returned session is not null
        assertNotNull(httpSession);
    }


    @Test
    public void testIfPostCanBeCreated() throws Exception {
        this.mvc.perform(post("/posts/create").with(csrf())
                .session((MockHttpSession) this.httpSession)
                .param("title", "a new post")
                .param("body", "this is the body of the post"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testShowPost() throws Exception {

        JobPost existingJobPost = postDao.findAll().get(0);

        // Makes a Get request to /posts/{id} and expect a redirection to the Posts show page
        this.mvc.perform(get("/posts/" + existingJobPost.getId()))
                .andExpect(status().isOk())
                // Test the dynamic content of the page
                .andExpect(content().string(containsString(existingJobPost.getDescription())))
                .andExpect(content().string(containsString(existingJobPost.getTitle())));
    }

    @Test
    public void testPostsIndex() throws Exception {
        JobPost existingJobPost = postDao.findAll().get(0);

        // Makes a Get request to /posts and verifies that we get some of the static text of the posts/company-index.html template and at least the title from the first Ad is present in the template.
        this.mvc.perform(get("/posts"))
                .andExpect(status().isOk())
                // Test the static content of the page
                .andExpect(content().string(containsString("Featured Posts")))
                // Test the dynamic content of the page
                .andExpect(content().string(containsString(existingJobPost.getTitle())));
    }

    @Test
    public void testEditPost() throws Exception {
        // Gets the first Ad for tests purposes
        JobPost existingJobPost = postDao.findAll().get(0);


        // Makes a JobPost request to /posts/{id}/edit and expect a redirection to the JobPost show page
        this.mvc.perform(
                post("/posts/" + existingJobPost.getId() + "/update").with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("title", "edited title")
                        .param("body", "edited body"))
                .andExpect(status().is3xxRedirection());

        // Makes a GET request to /posts/{id} and expect a redirection to the myPosts page
        this.mvc.perform(get("/posts/" + existingJobPost.getId()))
                .andExpect(status().isOk())
                // Test the dynamic content of the page
                .andExpect(content().string(containsString("edited title")))
                .andExpect(content().string(containsString("edited body")));
    }

    @Test
    public void testDeletePost() throws Exception {
        // Creates a test JobPost to be deleted
        this.mvc.perform(
                post("/posts/create").with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("title", "ad to be deleted")
                        .param("body", "won't last long"))
                .andExpect(status().is3xxRedirection());

        // Get the recent JobPost that matches the title
        JobPost existingJobPost = postDao.findByTitle("ad to be deleted");

        // Makes a JobPost request to /posts/{id}/delete and expect a redirection to the Posts index
        this.mvc.perform(
                post("/posts/" + existingJobPost.getId() + "/delete").with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("id", String.valueOf(existingJobPost.getId())))
                .andExpect(status().is3xxRedirection());
    }

//    @Test
//    public void contextLoads() {
//        // Sanity Test, just to make sure the MVC bean is working
//        assertNotNull(mvc);
//    }
//
//    @Test
//    public void testIfUserSessionIsActive() throws Exception {
//        // It makes sure the returned session is not null
//        assertNotNull(httpSession);
//    }
}
