package com.mvpnest.fleetmanagement.security;

import com.mvpnest.fleetmanagement.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }

}