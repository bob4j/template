package com.openu.controller;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@ManagedBean
@RequestScoped
public class LoginManager {

    public String doLogin() throws IOException, ServletException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/login");
        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }

    public String doLogout() throws ServletException, IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/logout");
        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();
        return "home";
    }

}
