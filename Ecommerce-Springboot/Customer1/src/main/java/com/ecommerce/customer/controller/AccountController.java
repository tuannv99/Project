package com.ecommerce.customer.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecommerce.library.model.City;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CityService;
import com.ecommerce.library.service.CustomerService;

@Controller
public class AccountController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CityService cityService;
	
		@GetMapping("/account")
		public String accountHome(Model model, Principal principal) {
			if(principal == null) {
				return "redirect:/login";
			}
			String username = principal.getName();
			Customer customer = customerService.findByUsername(username);
			model.addAttribute("customer", customer);
			return "my-account";
		}
		
		
		@GetMapping("/change-account") 
			public String changeAccount(Model model, Principal principal) {
				String username = principal.getName();
				if(principal == null) {
					return "redirect:/login";
				}
				Customer customer = customerService.findByUsername(username);
//				List<City> cityList = cityService.getAllCities();
				model.addAttribute("customer", customer);
//				model.addAttribute("cities", cityList);
				return "account-info-change";
			}
		
		@RequestMapping(value= "/update-info", method = {RequestMethod.GET, RequestMethod.PUT})
		public String updateCustomer(@ModelAttribute("customer")Customer customer,
				Model model) {
			Customer customerSaved = customerService.saveInfor(customer);
			
			model.addAttribute("customer", customerSaved);
			model.addAttribute("success", "Update info successfully!");
			return "account-info-change";
			
		}
		
}
