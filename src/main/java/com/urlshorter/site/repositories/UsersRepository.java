package com.urlshorter.site.repositories;

import com.urlshorter.site.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE user_email LIKE ?1 AND user_blocked = ?2 ORDER BY user_id", nativeQuery = true)
    List<User> findByEmailLikeAndBlocked(String emailFragment, boolean blocked);

    @Query(value = "SELECT * FROM users WHERE user_email LIKE ?1 AND user_role = ?2 AND user_blocked = ?3 ORDER BY user_id", nativeQuery = true)
    List<User> findByEmailLikeAndRoleAndBlocked(String emailFragment, String role, boolean blocked);


}
