package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;

public interface CustomerService {
	CustomerDto save(CustomerDto customerDto);
	
	Customer findByUsername(String username);
	
	Customer saveInfor(Customer customer);
	
	Customer changePassword(Customer customer, String newPassword);
}
