package com.urlshorter.site.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class AuthSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1,
                                        Authentication authentication) throws IOException {

        boolean AuthIsUser = false;
        boolean AuthIsAdmin = false;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {

            if (grantedAuthority.getAuthority().equals("ROLE_user")) {
                AuthIsUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_admin")) {
                AuthIsAdmin = true;
                break;
            }
        }

        if (AuthIsUser) {
            redirectStrategy.sendRedirect(arg0, arg1, "/user-lk");
        } else if (AuthIsAdmin) {
            redirectStrategy.sendRedirect(arg0, arg1, "/admin-lk");
        } else {
            throw new IllegalStateException();
        }
    }

}
