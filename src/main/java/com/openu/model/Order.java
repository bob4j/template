package com.openu.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity(name = "order_")
public class Order {

    private static final String ORDER_SEQ = "order_seq";

    public static final String ORDER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Id
    @SequenceGenerator(name = ORDER_SEQ, sequenceName = ORDER_SEQ, allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ORDER_SEQ)
    private Long id;

    private Long created;

    private Long modified;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
    private CreditCardInfo ccInfo;

    @Embedded
    private Address shippingAddress;
    
    private String phoneNumber;
    
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
        created = System.currentTimeMillis();
        modified = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public String getCreatedPretty() {
        return toPrettyDate(created);
    }

    public String getModifiedPretty() {
        return toPrettyDate(modified);
    }

    private static String toPrettyDate(Long date) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(ORDER_DATE_FORMAT);
        return df.format(new Date(date));
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public CreditCardInfo getCcInfo() {
        return ccInfo;
    }

    public void setCcInfo(CreditCardInfo ccInfo) {
        this.ccInfo = ccInfo;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return getItems().stream().map(i -> i.getTotalPrice()).reduce(0D, (accumulator, i) -> accumulator + i);
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }
    
   

}
