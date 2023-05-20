package com.ecommerce.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("title", "Login");
		return "login";
	}
	
	@RequestMapping("/index")
	public String home(Model model) {
		model.addAttribute("title", "Home Page");
		return "index";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("title", "Register");
		model.addAttribute("adminDto", new AdminDto());
		return "register";
	}
	
	@PostMapping("/register")
	public String addnewAdmin(@Valid @ModelAttribute("adminDto")AdminDto adminDto,
			BindingResult result,
			Model model) {
		try {
			if(result.hasErrors()) {
				model.addAttribute("adminDto", adminDto);
				return "register";
			}
			String username = adminDto.getUsername();
			Admin admin = adminService.findByUsername(username);
			if(admin != null) {
				model.addAttribute("adminDto", adminDto);
				model.addAttribute("errorEmail", "Your email have been registerd!");
				return "register";
			}
			if(!adminDto.getPassword().equals(adminDto.getRepeatPassword())) {
				model.addAttribute("errorPassword", "Password is not same");
				return "register";
			}else {
				adminService.save(adminDto);
				model.addAttribute("adminDto", adminDto);
				model.addAttribute("success", "Register successfully!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Error server");
		}
		
		
		return "register";
	}
	
	@GetMapping("/forgot-password")
	public String forgotPassword(Model model) {
		model.addAttribute("title", "Forgot Password");
		return "forgot-password";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
	    return "login";  //Where you go after logout here.
	}
	
	
	
}
