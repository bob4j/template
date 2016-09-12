package com.openu.controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
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
import com.openu.repository.OrderRepository;
import com.openu.util.CustomerTransaction;

@Component
@Scope("view")
public class CheckoutController implements Serializable {

	private static final long serialVersionUID = 1L;

	private CreditCardInfo ccInfo;
	private Address shippingAddress;
	private Long cityId;
	private Boolean acceptTerms;
	private Boolean useCustomerAddress = true;
	private String phoneNumber;

	@Resource
	private SessionBean sessionBean;
	@Resource
	private CustomerRepository customerRepository;
	@Resource
	private CityRepository cityRepository;
	@Resource
	private OrderRepository orderRepository;

	@PostConstruct
	public void init() {
		ccInfo = new CreditCardInfo();
		Address customerAddress = sessionBean.loadCustomer().getAddress();
		if (customerAddress != null && Boolean.TRUE.equals(useCustomerAddress)) {
			shippingAddress = new Address(customerAddress.getCity(), customerAddress.getAddress());
		} else {
			shippingAddress = new Address();
		}
	}

	@Transactional
	public Double getTotalPrice() {
		Customer customer = customerRepository.findOne(sessionBean.getCustomer().getId());
		Order order = customer.getShoppingCart();
		return order.getTotalPrice();
	}

	@Transactional
	@CustomerTransaction
	public void order() {
		Customer customer = sessionBean.loadCustomer();
		Order order = customer.getShoppingCart();
		order.setStatus(OrderStatus.PLACED);
		order.setCcInfo(ccInfo);
		order.setModified(System.currentTimeMillis());
		order.setShippingAddress(getAddress(customer));
		customerRepository.save(customer);
	}

	private Address getAddress(Customer customer) {
		if (Boolean.TRUE.equals(useCustomerAddress)) {
			return customer.getAddress();
		}
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

	public Boolean getAcceptTerms() {
		return acceptTerms;
	}

	public void setAcceptTerms(Boolean acceptTerms) {
		this.acceptTerms = acceptTerms;
	}

	public Boolean getUseCustomerAddress() {
		return useCustomerAddress;
	}

	public void setUseCustomerAddress(Boolean useCustomerAddress) {
		this.useCustomerAddress = useCustomerAddress;
	}

	public boolean ShowAdressField() {
		return !useCustomerAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
