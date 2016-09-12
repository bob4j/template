package com.openu.util;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.openu.controller.SessionBean;
import com.openu.model.Customer;

/**
 * Implements customer-based mutual exclusion
 */
@Component
@Aspect
public class CustomerTransactionAspect {

	@Resource
	private PlatformTransactionManager txManager;

	@Resource
	private SessionBean sessionBean;

	@Around("@annotation(com.openu.util.CustomerTransaction)")
	public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
		Customer customer = sessionBean.getCustomer();
		if (customer == null) {
			return pjp.proceed();
		}
		try {
			NamedLock.lock(customer.getUsername());
			return pjp.proceed();
		} finally {
			NamedLock.unlock(customer.getUsername());
		}
	}

}