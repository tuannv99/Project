package com.ecommerce.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.service.OrderDetailService;
import com.ecommerce.library.service.OrderService;

@Controller
public class AdminController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@GetMapping("/orders")
	public String findAllOrder(Model model) {
		List<Order> listOrder = orderService.findAllOrder();
		model.addAttribute("list", listOrder);
		model.addAttribute("size", listOrder.size());
		return "redirect:/orders/0";
	}
	
	@GetMapping("/orders/{pageNo}")
	public String findAllOrder(
			@PathVariable("pageNo")int pageNo,
			Model model) {
		Page<Order> order = orderService.pageOrder(pageNo);
		model.addAttribute("totalPages", order.getTotalPages());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("size", order.getSize());
		model.addAttribute("list", order);
		System.out.println(order.getSize());
		System.out.println(order.toString());
//		model.addAttribute("list", listOrder);

		return "orders";
	}
	
	@GetMapping("/accept/{id}")
	public String acceptOrder(@PathVariable("id")Long id,RedirectAttributes attributes) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if(auth!= null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
					orderService.acceptOrder(id);
					attributes.addFlashAttribute("success", "Accept successfully!");
					return "redirect:/orders";
				}else {
					attributes.addFlashAttribute("error", "You must be login with role ADMIN to access this page");
					return "redirect:/login";
				}
			
		
	}
	
	@GetMapping("/cancel/{id}")
	public String cancelOrder(@PathVariable("id")Long id, RedirectAttributes attributes) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!= null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			orderService.cancelOrder(id);
			attributes.addFlashAttribute("success", "Order has been cancel!");
			return "redirect:/orders";
		}else {
			attributes.addFlashAttribute("error", "You must be login with role ADMIN to access this page");
			return "redirect:/login";
		}

	}
	
	@GetMapping("/orderDetail/{id}")
	public String findOrder(@PathVariable("id")Long orderId,Model model) {
		List<OrderDetails> orderDetails = orderDetailService.findOrderDetailsByOrderId(orderId);
		model.addAttribute("orderDetail", orderDetails);
		return "orders-detail";
	}
	
	
	
}
