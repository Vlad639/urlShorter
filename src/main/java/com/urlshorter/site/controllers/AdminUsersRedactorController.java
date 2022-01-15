package com.urlshorter.site.controllers;

import com.urlshorter.site.models.User;
import com.urlshorter.site.other.SearchUsersParameters;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin-lk/users-redaktor")
public class AdminUsersRedactorController {

    @Autowired
    UsersRepository usersRepository;

    SearchUsersParameters searchUsersParameters;

    @RequestMapping("")
    String adminUsersRedactor(){

        return "users-redaktor";
    }

    @RequestMapping("/set-user-blocked")
    ModelAndView setUserActive(
            @RequestParam("user_id") Long userId,
            @RequestParam("user_blocked") boolean userBlocked
    ){

        User user = usersRepository.getById(userId);
        user.setBlocked(userBlocked);
        usersRepository.save(user);

        return searchUsers(searchUsersParameters.emailFragment,
                searchUsersParameters.role,
                searchUsersParameters.blocked);
    }

    @RequestMapping("/search")
    ModelAndView searchUsers(
            @RequestParam("user_email") String emailFragment,
            @RequestParam("user_role") String role,
            @RequestParam("user_blocked") boolean blocked
    ){
        ModelAndView modelAndView = new ModelAndView("users-redaktor::users_table");
        List<User> foundedUsers;

        if (role.equals("all")){
            foundedUsers = usersRepository.findByEmailLikeAndBlocked("%" + emailFragment + "%", blocked);
        } else
            foundedUsers = usersRepository.findByEmailLikeAndRoleAndBlocked("%" + emailFragment + "%", role, blocked);

        searchUsersParameters = new SearchUsersParameters(emailFragment, role, blocked);

       modelAndView.addObject("foundedUsers", foundedUsers);

        return modelAndView;
    }

    @RequestMapping("/set-user-role")
    ModelAndView setUserRole(
            @RequestParam("user_id") Long userId,
            @RequestParam("user_role") String userRole
    ){
        User user = usersRepository.getById(userId);
        user.setRole(userRole);
        usersRepository.save(user);

       return searchUsers(searchUsersParameters.emailFragment,
                          searchUsersParameters.role,
                          searchUsersParameters.blocked);
    }

}
