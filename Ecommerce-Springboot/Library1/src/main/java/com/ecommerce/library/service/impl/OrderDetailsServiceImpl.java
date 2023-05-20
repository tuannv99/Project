package com.ecommerce.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.service.OrderDetailService;
@Service
public class OrderDetailsServiceImpl implements OrderDetailService{
	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Override
	public List<OrderDetails> findOrderDetailsByOrderId(Long orderId) {

		return orderDetailRepository.findOrderDetailsByOrderId(orderId);
	}
	
	


}
