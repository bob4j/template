package com.openu.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.openu.model.User;

@ManagedBean
@Component
@SessionScoped
public class SessionBean {

    private User user;

    public User getUser() {
        if (user == null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User) {
                user = (User) principal;
            }
        }
        return user;
    }
}
