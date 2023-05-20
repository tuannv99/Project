package com.ecommerce.library.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Provider;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.MailService;
@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private  MailService mailService;

	
	@Override
	public Customer findByUsername(String username) {

		return customerRepository.findByUsername(username);
	}

	@Override
	public CustomerDto save(CustomerDto customerDto) {
		Customer customer =  new Customer();
		customer.setFirstName(customerDto.getFirstName());
		customer.setLastName(customerDto.getLastName());
		customer.setUsername(customerDto.getUsername());
		customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
		customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
		customer.setProviderId(Provider.local.name());
		//send mail
		Customer customerSave = customerRepository.save(customer);
		mailService.sendMailCreateCustomer(customerDto);
		return mapperDTO(customerSave);
	}
	
	private CustomerDto mapperDTO(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setFirstName(customer.getFirstName());
		customerDto.setLastName(customer.getLastName());
		customerDto.setUsername(customer.getUsername());
		customerDto.setPassword(customer.getPassword());
		return customerDto;
	}

	@Override
	public Customer saveInfor(Customer customer) {
		Customer customer1 = customerRepository.findByUsername(customer.getUsername());
		customer1.setAddress(customer.getAddress());
		customer1.setCity(customer.getCity());
		customer1.setCountry(customer.getCountry());
		customer1.setPhoneNumber(customer.getPhoneNumber());
		return customerRepository.save(customer1);
	}



}
