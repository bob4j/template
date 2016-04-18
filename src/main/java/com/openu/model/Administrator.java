package com.openu.model;

import javax.persistence.Entity;

import com.google.common.collect.Lists;

@Entity
public class Administrator extends User {

    private String name;

    private String email;

    public Administrator() {
        super();
        roles = Lists.newArrayList(Role.ADMIN);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
