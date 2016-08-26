package com.openu.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class CreditCardInfo {

    private static final int INITIAL_VALUE = 100;
    private static final int ALLOCATED_SIZE = 1;
    private static final String CREDIT_CARD_INFO_SEQUENCE_NAME = "cc_info_seq";

    @Id
    @SequenceGenerator(name = CREDIT_CARD_INFO_SEQUENCE_NAME, sequenceName = CREDIT_CARD_INFO_SEQUENCE_NAME, allocationSize = ALLOCATED_SIZE, initialValue = INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CREDIT_CARD_INFO_SEQUENCE_NAME)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CreditCardType type;

    private String number;

    private String ownerName;

    private String cvv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

}
