package com.ecommerce.customer.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/cart")
	public String cart(Model model, Principal principal) {
		if(principal == null) {
			return "redirect:/login";
		}
		String username = principal.getName();
		Customer customer = customerService.findByUsername(username);
		ShoppingCart shoppingCart = customer.getShoppingCart();
		if(shoppingCart == null) {
			model.addAttribute("check", "No items in your cart");
			return "redirect:/home";
		}
			double subTotal = shoppingCart.getTotalPrices();
			model.addAttribute("subTotal", shoppingCart.getTotalPrices());
			model.addAttribute("shoppingCart", shoppingCart);
		
		
		
		
		return "cart";
	}
	@PostMapping("/add-to-cart")
	public String addItemToCart(@RequestParam("id")Long productId,
			@RequestParam(value = "quantity", required = false, defaultValue = "1")int quantity,
			Model model,
			Principal principal
			,HttpServletRequest request
			,HttpSession session) {
			if(principal == null) {
				return "redirect:/login";
			}
			Product product = productService.getProductById(productId);
			String username = principal.getName();
			Customer customer = customerService.findByUsername(username);
			ShoppingCart cart = shoppingCartService.addItemToCart(product, quantity, customer);
			session.setAttribute("totalItems", cart.getTotalItems());
			
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping( value = "/update-cart", method = RequestMethod.POST ,params = "action=update")
	public String updateCart(@RequestParam("quantity")int quantity,
			@RequestParam("id")Long productId
			,Model model,
			Principal principal,
			HttpSession session) {
		if(principal == null) {
			return "redirect:/login";
		}
		String username = principal.getName();
		Customer customer = customerService.findByUsername(username);
		Product product = productService.getProductById(productId);
		ShoppingCart cart = shoppingCartService.updateItemInCart(product, quantity, customer);
		session.setAttribute("totalItems", cart.getTotalItems());
		model.addAttribute("shoppingCart", cart);
		
		return "redirect:/cart";
		
	}
	
	@RequestMapping(value = "/update-cart",method = RequestMethod.POST, params = "action=delete")
	public String deleteItemFromCart(@RequestParam("id") Long productId,
			Model model,
			Principal principal,
			HttpSession session) {
		if(principal == null) {
			return "redirect:/login";
		}
		
		String username = principal.getName();
		Customer customer = customerService.findByUsername(username);
		Product product = productService.getProductById(productId);
		ShoppingCart cart = shoppingCartService.deleteItemFromCart(product, customer);
		session.setAttribute("totalItems", cart.getTotalItems());
		model.addAttribute("shoppingCart", cart);
		return "redirect:/cart";
	}
}
