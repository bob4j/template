package com.openu.model;

import javax.persistence.Entity;

import com.google.common.collect.Lists;

@Entity
public class Administrator extends User {

    private String name;


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

}
