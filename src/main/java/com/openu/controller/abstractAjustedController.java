package com.openu.controller;

import org.springframework.security.core.context.SecurityContextHolder;

import com.openu.model.Administrator;
import com.openu.model.Customer;
/**
 * 
 * @author shaharb
 *  abstract class for Action and Forms that their behavior depends on the user Identity (e.g. product details)
 */
public abstract class abstractAjustedController {

	public String getCustomer() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof Customer) {
			return ((Customer) principal).getFirstName();
		}
		return null;
	}

	public Administrator getAdmin() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof Administrator) {
			return (Administrator) principal;
		}
		return null;
	}

}
