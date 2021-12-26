package com.urlshorter.site.repositories;

import com.urlshorter.site.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
    
}
