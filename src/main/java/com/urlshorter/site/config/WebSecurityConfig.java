package com.urlshorter.site.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthSuccessHandler authenticationSuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/admin-lk/**").hasAuthority("admin")
                    .antMatchers("/","/jQuery.js","/confirm-account", "/images/**","/registration","/reg-new-user", "/css/user-lk.css", "/short/**").permitAll()
                    .antMatchers("/login", "/login-fail", "/reset-password", "/get-new-pass").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .loginProcessingUrl("/perform-login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(new CustomAuthenticationFailureHandler())
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();


    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("SELECT user_email, user_password, user_active FROM users WHERE user_email = ?")
                .authoritiesByUsernameQuery("SELECT user_email, user_role FROM users WHERE user_email = ?");
    }
}
