package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.model.OrderDetails;

public interface OrderDetailService {
	List<OrderDetails> findOrderDetailsByOrderId(Long orderId);
}
