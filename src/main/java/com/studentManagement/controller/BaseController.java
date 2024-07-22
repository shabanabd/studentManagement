package com.studentManagement.controller;

import com.studentManagement.core.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public abstract class BaseController {

    protected UserPrincipal getCurrentUser() {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authenticationToken.getPrincipal();
    }
}
