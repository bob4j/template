package com.openu.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class City {



    private static final String CITY_SEQUENCE_NAME = "city_seq";

    @Id
    @SequenceGenerator(name = CITY_SEQUENCE_NAME, sequenceName = CITY_SEQUENCE_NAME, allocationSize = 1, initialValue =100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CITY_SEQUENCE_NAME)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
