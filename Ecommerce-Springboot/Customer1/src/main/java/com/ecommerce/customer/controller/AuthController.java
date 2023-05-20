package com.ecommerce.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.library.dto.CustomerDto;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;

import jakarta.validation.Valid;

@Controller
public class AuthController {
	
	@Autowired
	private CustomerService customerService;
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping("/register")
	public String register(Model model) {
		
		model.addAttribute("customerDto", new CustomerDto());
		return "register";
	}
	
	@PostMapping("/do-register")
	public String processRegister(@Valid @ModelAttribute("customerDto")CustomerDto customerDto
			,BindingResult result,
			Model model
			) {
		try {
			if(result.hasErrors()) {
				return "register";
			}
			String username = customerDto.getUsername();
			Customer customer = customerService.findByUsername(username);
			if(customer != null) {
				model.addAttribute("customerDto", customerDto);
				model.addAttribute("errorEmail", "Your email have been registerd!");
				return "register";
			}
			
			if(!customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
				model.addAttribute("customerDto", customerDto);
				model.addAttribute("errorPassword", "Password is not same");
				return "register";
			}else {
				customerService.save(customerDto);
				model.addAttribute("success", "Register successfully!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Error server");
		}
		
		return "register";
	}
}
