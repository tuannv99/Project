package com.ecommerce.customer.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/check-out")
	public String checkout(Model model, Principal principal) {
		
		String username = principal.getName();
		
		Customer customer = customerService.findByUsername(username);
		
		if(customer.getPhoneNumber() == null || customer.getAddress() == null || customer.getCity() == null
				|| customer.getCountry() == null) {
			model.addAttribute("customer", customer);
			model.addAttribute("error", "You must fill the infomation to check out!");
			return "account-info-change";
		}
		
		
		model.addAttribute("customer", customer);
		ShoppingCart cart = customer.getShoppingCart();
		model.addAttribute("cart", cart);
		
		return "checkout";
	}
	
	@GetMapping("/order")
	public String order(Model model, Principal principal) {
		String username = principal.getName();
		Customer customer = customerService.findByUsername(username);
		List<Order> orderList = customer.getOrders();
		model.addAttribute("orders", orderList);
		return "order";
	}
	
	@GetMapping("/save-order")
	public String saveOrder(Principal principal,Model model,HttpSession session) {
		String username = principal.getName();
		Customer customer = customerService.findByUsername(username);
		ShoppingCart cart = customer.getShoppingCart();
		orderService.saveOrder(cart);
//		model.addAttribute("totalItems", cart.getTotalItems());
		session.removeAttribute("totalItems");
		
		return "redirect:/order";
	}
}
