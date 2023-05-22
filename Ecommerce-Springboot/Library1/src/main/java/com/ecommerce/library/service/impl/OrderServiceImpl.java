package com.ecommerce.library.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.OrderService;
@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Override
	public void saveOrder(ShoppingCart cart) {
		Order order = new Order();
		order.setOrderStatus("PENDING");
		order.setOrderDate(new Date());
		order.setCustomer(cart.getCustomer());
		order.setTotalPrice(cart.getTotalPrices());
		List<OrderDetails> orderDetailsList = new ArrayList<>();
		for(CartItem item : cart.getCartItem()) {
			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setOrder(order);
			orderDetails.setQuantity(item.getQuantity());
			orderDetails.setProduct(item.getProduct());
			orderDetails.setUnitPrice(item.getProduct().getCostPrice());
			orderDetails.setTotalPrice(item.getTotalPrice());
			orderDetailRepository.save(orderDetails);
			orderDetailsList.add(orderDetails);
			cartItemRepository.delete(item);
		}
		order.setOrderDetails(orderDetailsList);
//		order.setShippingFee();
		cart.setCartItem(new HashSet<>());
		cart.setTotalItems(0);
		cart.setTotalPrices(0.0);
		orderRepository.save(order);
		
		shoppingCartRepository.save(cart);
		
	}

	@Override
	public void acceptOrder(Long id) {
		Order order = orderRepository.findById(id).get();
		order.setDeliveryDate(new Date());
		order.setOrderStatus("SHIPPING");
		orderRepository.save(order);
		
	}

	@Override
	public void cancelOrder(Long id) {
		Order order = orderRepository.findById(id).get();
		order.setDeliveryDate(new Date());
		order.setOrderStatus("ORDER CANCEL");
		orderRepository.save(order);
		
	}

	@Override
	public Page<Order> pageOrder(int pageNo) {
		Pageable pageable = PageRequest.of(pageNo, 3);
		List<Order> orders = orderRepository.findAllOrder();
		Page<Order> orderPage = toPage(orders, pageable);
		
		return orderPage;
	}

	@Override
	public Order findById(Long id) {
		return orderRepository.findById(id).get();
	}
	
	private Page<Order> toPage(List<Order> list, Pageable pageable){
		if(pageable.getOffset() >= list.size()) {
			return Page.empty();
		}
		int startIndex = (int) pageable.getOffset();
		int endIndex = (int) ((pageable.getOffset())+ pageable.getPageSize() > list.size() 
				? list.size()
				: pageable.getOffset() + pageable.getPageSize());
		List<Order> subList = list.subList(startIndex, endIndex);
		return new PageImpl<>(subList,pageable,list.size());
	}

	@Override
	public List<Order> findAllOrder() {
		
		return orderRepository.findAllOrder();
	}


	
}
