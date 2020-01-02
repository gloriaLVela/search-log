package com.codeup.blog.blog.repositories;

import com.codeup.blog.blog.models.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<JobPost, Long> {
    JobPost findByTitle(String title);
}



