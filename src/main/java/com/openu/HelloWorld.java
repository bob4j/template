package com.openu;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ManagedBean(name = "helloWorld", eager = true)
@RequestScoped
@Component
public class HelloWorld {

    @Autowired
    private SpringService springService;

    public HelloWorld() {
        System.out.println("HelloWorld started!");
    }

    public String getMessage() {
        return springService.getName();
    }

}
