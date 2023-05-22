package com.ecommerce.customer.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired(required = true)
	private CategoryService categoryService;
	@Autowired(required = true)
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	

	
	@RequestMapping(value = {"/index","/"}, method = RequestMethod.GET)
	public String index(Model model,Principal principal,HttpSession session) {
		if(principal != null ) {
			session.setAttribute("username", principal.getName());
			Customer customer = customerService.findByUsername(principal.getName());
			ShoppingCart cart = customer.getShoppingCart();
			if(cart == null) {
				cart = new ShoppingCart();
			}
			session.setAttribute("totalItems", cart.getTotalItems());
		}else {
			session.removeAttribute("username");
		}
		return "home";
	}
	@GetMapping("/home")
	public String home(Model model) {
		List<Category> categories = categoryService.findAll();
		List<ProductDto>products = productService.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		return "index";
	}
	
	@GetMapping("/contact-us")
	public String contact() {
		return "contact-us";
	}
}
