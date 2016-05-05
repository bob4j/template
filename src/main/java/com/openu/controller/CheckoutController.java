package com.openu.controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.openu.model.Address;
import com.openu.model.City;
import com.openu.model.CreditCardInfo;
import com.openu.model.CreditCardType;
import com.openu.model.Customer;
import com.openu.model.Order;
import com.openu.model.OrderStatus;
import com.openu.repository.CityRepository;
import com.openu.repository.CustomerRepository;

@Component
@Scope("view")
public class CheckoutController implements Serializable {

    private static final long serialVersionUID = 1L;

    private CreditCardInfo ccInfo;
    private Address shippingAddress;
    private Long cityId;
    @Resource
    private SessionBean sessionBean;
    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private CityRepository cityRepository;

    public CheckoutController() {
        ccInfo = new CreditCardInfo();
        shippingAddress = new Address();
    }

    @Transactional
    public Double getTotalPrice() {
        Customer customer = customerRepository.findOne(sessionBean.getCustomer().getId());
        Order order = customer.getShoppingCart();
        // return order.getItems().stream().map(i -> i.getTotalPrice()).reduce(0D, (accumulator, i) -> accumulator + i);
        return order.getTotalPrice();
    }

    @Transactional
    public void order() {
        Customer customer = customerRepository.findOne(sessionBean.getCustomer().getId());
        Order order = customer.getShoppingCart();
        order.setStatus(OrderStatus.PLACED);
        order.setCcInfo(ccInfo);
        order.setModified(System.currentTimeMillis());
        order.setShippingAddress(getAddress());
        customerRepository.save(customer);
    }

    private Address getAddress() {
        shippingAddress.setCity(cityRepository.findOne(cityId));
        return shippingAddress;
    }

    public List<CreditCardType> getCcTypes() {
        return Stream.of(CreditCardType.values()).collect(Collectors.toList());
    }

    public List<City> getCities() {
        return (List<City>) cityRepository.findAll();
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
