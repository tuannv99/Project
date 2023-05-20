package com.ecommerce.customer.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	

	@GetMapping("/products")
	public String products(Model model,Principal principal) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CUSTOMER"))) {
			List<Product> products = productService.getAllProducts();
			List<Product> productView = productService.listViewProducts();
			List<CategoryDto> categoryDtoList = categoryService.getCategoryAndProduct();
			model.addAttribute("categories", categoryDtoList);
			model.addAttribute("productView", productView);
			model.addAttribute("products", products);
			return "shop";

	}
	
	@GetMapping("/find-product/{id}")
	public String findProductById(@PathVariable("id")Long id, Model model) {
		Product product = productService.getProductById(id);
		Long categoryId = product.getCategory().getId();
		List<Product> products = productService.getRelatedProduct(categoryId);
		model.addAttribute("product", product);
		model.addAttribute("products", products);
		
		return "product-detail";
	}
	
	@GetMapping("/products-in-category/{id}")
	public String getProductsInCategory(@PathVariable("id")Long categoryId,Model model) {
		Category category = categoryService.getById(categoryId);
		List<CategoryDto> categories = categoryService.getCategoryAndProduct();
		List<Product> products = productService.getProductsInCategory(categoryId);
		model.addAttribute("categories", categories);
		model.addAttribute("category", category);
		model.addAttribute("products", products);
		
		return "products-in-category";
	}
	
	@GetMapping("/high-price")
	public String filterHighPrice(Model model) {
		List<Category>categories = categoryService.findAllByActivated();
		List<Product> products = productService.filterHighToLow();
		List<CategoryDto>categoryDtoList = categoryService.getCategoryAndProduct();
		model.addAttribute("categories", categoryDtoList);
		model.addAttribute("products", products);
		model.addAttribute("category", categories);
		return "filter-high";
	}
	
	@GetMapping("/low-price")
	public String filterLowPrice(Model model) {
		List<Product> products = productService.filterLowToHigh();
		List<Category>categories = categoryService.findAllByActivated();
		List<CategoryDto>categoryDtoList = categoryService.getCategoryAndProduct();
		model.addAttribute("products", products);
		model.addAttribute("category", categories);
		model.addAttribute("categories", categoryDtoList);
		return "filter-low";
	}
	
	
	
	
}
