package com.codeup.blog.blog.repositories;

import com.codeup.blog.blog.models.Company;
import com.codeup.blog.blog.models.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByName(String name);
}
