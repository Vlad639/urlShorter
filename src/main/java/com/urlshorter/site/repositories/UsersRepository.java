package com.urlshorter.site.repositories;

import com.urlshorter.site.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {

}
