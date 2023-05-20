package com.ecommerce.library.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.model.ShoppingCart;

public interface OrderService {
	void saveOrder(ShoppingCart cart);
	
	void acceptOrder(Long id);
	
	void cancelOrder(Long id);
	
	List<Order> findAllOrder();
	
	Page<Order> pageOrder(int pageNo);
	
	Order findById(Long id);
}
