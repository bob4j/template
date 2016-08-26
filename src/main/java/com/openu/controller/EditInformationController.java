package com.openu.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class EditInformationController extends AbstractCustomerInformationCollector  {

    private static final long serialVersionUID = -7669809489656184652L;
    @Resource
    private SessionBean sessionBean;

    @PostConstruct
    public void prepareEditing() {
       this.customer = sessionBean.loadCustomer();
       if (customer.getAddress() != null){
	   setAddressLine(customer.getAddress().getAddress());
	   setCityId(customer.getAddress().getCity().getId());
       }
       setEmail(customer.getEmail());
       setFirstName(customer.getFirstName());
       setLastName(customer.getLastName());
       setPassword(customer.getPassword());
       setPasswordAgain(customer.getPassword());
       setPhoneNumber(customer.getPhoneNumber());
       setUsername(customer.getUsername());
       
    }
    
}
