package com.ecommerce.admin.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/categories")
	public String categories(Model model) {
		
		System.out.println("Bạn chưa đăng nhập");
		model.addAttribute("title", "Manage Category");
		List<Category>categories = categoryService.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("size", categories.size());
		model.addAttribute("categoryNew", new Category());
		return "categories";
	}
	
	@PostMapping("/add-category")
	public String add(@ModelAttribute("categoryNew")Category category
			,RedirectAttributes attributes) {

		try {
			Category cateExisted = categoryService.findByName(category.getName());
			if(cateExisted != null) {
				attributes.addFlashAttribute("error", "Dulicate value");
				return "redirect:/categories";
			}
			categoryService.save(category);
			attributes.addFlashAttribute("success", "Add success fully!");
		}
		catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Cant't not add category!Please try again");
		}
		return "redirect:/categories";
	}
	
	@GetMapping("/edit-category/{id}")
	public String editCategory(@PathVariable("id")Long id, Model model) {
		model.addAttribute("title", "Edit Category");
		Category category = categoryService.getById(id);
		model.addAttribute("category", category);
		return "edit-category";
	}
	
	@PostMapping("/edit-category/{id}")
	public String editProsess(@PathVariable("id") Long id
			,@ModelAttribute("category")Category category,
			RedirectAttributes attributes) {
		try {
			categoryService.update(category);
			attributes.addFlashAttribute("success", "Edit successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("failed", "Can't not to update category");
		}
		
		return "redirect:/categories";
	}
	
	@GetMapping("/enableById/{id}")
	public String enableById(@PathVariable("id")Long id, RedirectAttributes attributes) {
		try {
			categoryService.enableById(id);
			attributes.addFlashAttribute("success", "Enable successfully!");
			return "redirect:/categories";
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "can't not enable");
		}
		return "redirect:/categories";
		
	}
	@GetMapping("/deleteById/{id}")
	public String deleteById(@PathVariable("id")Long id, RedirectAttributes attributes) {
		try {
			categoryService.deleteById(id);
			attributes.addFlashAttribute("success", "Delete successfully!");
			return "redirect:/categories";
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Can't not delete ");

		}
		return "redirect:/categories";
		
	}
	
	
	
	
	
	
}
