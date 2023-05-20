package com.ecommerce.library.service.impl;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingcartRepository;

	@Override
	public ShoppingCart addItemToCart(Product product, int quantity, Customer customer) {
		ShoppingCart cart = customer.getShoppingCart();
		if(cart == null) {
			cart = new ShoppingCart();
		}
		
		Set<CartItem>cartItems = cart.getCartItem();
		CartItem cartItem = findCartItem(cartItems, product.getId());
		
		if(cartItems == null) {
			cartItems = new HashSet<>();
			if(cartItem == null) {
				cartItem = new CartItem();
				cartItem.setProduct(product);
				cartItem.setQuantity(quantity);
				cartItem.setTotalPrice(quantity * product.getCostPrice());
				cartItem.setCart(cart);
				cartItems.add(cartItem);
				cartItemRepository.save(cartItem);
			}
		}else {
				if(cartItem == null) {
					cartItem = new CartItem();
					cartItem.setProduct(product);
					cartItem.setQuantity(quantity);
					cartItem.setTotalPrice(quantity * product.getCostPrice());
					cartItem.setCart(cart);
					cartItems.add(cartItem);
					cartItemRepository.save(cartItem);
				}else {
					cartItem.setQuantity(cartItem.getQuantity() + quantity);
					cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * product.getCostPrice()));
					cartItems.add(cartItem);
					cartItemRepository.save(cartItem);
				}
			}
			int totalItems = totalItems(cartItems);
			double totalPrice = totalPrice(cartItems);
			
			cart.setTotalItems(totalItems);
			cart.setTotalPrices(totalPrice);
			cart.setCustomer(customer);	
	
			return shoppingcartRepository.save(cart);
	}

	
	private CartItem findCartItem(Set<CartItem>cartItems,Long productId) {
		if(cartItems == null) {
			return null;
		}
		CartItem cartItem = null;
		
		for(CartItem item : cartItems) {
			if(item.getProduct().getId() == productId) {
				cartItem = item;
			}
		}
		return cartItem;
	}
	
	
	private int totalItems(Set<CartItem>cartItems) {
		int totalItems = 0;
		for(CartItem item : cartItems) {
			totalItems += item.getQuantity();
		}
		return totalItems;
	}
	
	private double totalPrice(Set<CartItem>cartItems) {
		double totalPrices = 0.0;
		for(CartItem item : cartItems) {
			totalPrices += item.getTotalPrice();
		}
		return totalPrices;
	}


	@Override
	public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {
		ShoppingCart cart = customer.getShoppingCart();
		
		Set<CartItem> cartItems = cart.getCartItem();
		CartItem item = findCartItem(cartItems, product.getId());
		
		item.setQuantity(quantity);
		item.setTotalPrice(quantity * product.getCostPrice());
		cartItemRepository.save(item);
		
		int totalItems = totalItems(cartItems);
		double totalPrice = totalPrice(cartItems);
		
		cart.setTotalItems(totalItems);
		cart.setTotalPrices(totalPrice);
		cart.setCustomer(customer);
		
		return shoppingcartRepository.save(cart);
	}


	@Override
	public ShoppingCart deleteItemFromCart(Product product, Customer customer) {
		ShoppingCart cart = customer.getShoppingCart();
		Set<CartItem> cartItems = cart.getCartItem();
		CartItem item = findCartItem(cartItems, product.getId());
		
		cartItems.remove(item);
		cartItemRepository.delete(item);
		
		int totalItems = totalItems(cartItems);
		double totalPrice = totalPrice(cartItems);
		
		cart.setTotalItems(totalItems);
		cart.setTotalPrices(totalPrice);
		cart.setCustomer(customer);
		
		return shoppingcartRepository.save(cart);
	}
	


}
