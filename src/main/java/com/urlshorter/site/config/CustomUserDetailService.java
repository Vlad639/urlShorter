package com.urlshorter.site.config;

import com.urlshorter.site.controllers.UsersController;
import com.urlshorter.site.models.User;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    PassEncoder passEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userSite = usersRepository.findByEmail(username);

        if (userSite == null) {
            throw new UsernameNotFoundException(username);
        }

        UsersController.SetUser(userSite);

        return org.springframework.security.core.userdetails.User.builder()
                .username(userSite.getEmail())
                .password(userSite.getPassword())
                .roles(userSite.getRole())
                .build();
    }
}

