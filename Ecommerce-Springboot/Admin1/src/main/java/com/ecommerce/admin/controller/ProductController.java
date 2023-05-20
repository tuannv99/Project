package com.ecommerce.admin.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.library.dto.ProductDto;
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
	public String product(Model model,Principal principal) {
		if(principal == null) {
			return "redict:/login";
		}
		System.out.println(principal.getName());
		model.addAttribute("title", "List Products");
		List<ProductDto> productDtoList = productService.findAll();
		model.addAttribute("products", productDtoList);
		model.addAttribute("size", productDtoList.size());
		return "redirect:/products/0";
	}
	
	@GetMapping("/add-product")
	public String addProduct(Model model,Principal principal) {
		if(principal == null) {
			return "redirect:/login";
		}
		List<Category>categories = categoryService.findAllByActivated();
		model.addAttribute("categories", categories);
		model.addAttribute("product", new ProductDto());
		return "add-products";
	}
	
	@PostMapping("save-product")
	public String saveProduct(
			@ModelAttribute("product")ProductDto productDto,
			@RequestParam("imageProduct")MultipartFile imageProduct
			,RedirectAttributes attributes) {
			
		try {
			productService.save(imageProduct, productDto);
			attributes.addFlashAttribute("success", "Add successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Failed to add!");
		}
		
		return "redirect:/products/0";
	}
	
	@GetMapping("/update-product/{id}")
	public String updateForm(@PathVariable("id")Long id, Model model, Principal principal) {
		if(principal == null) {
			return "redirect:/login";
		}
		model.addAttribute("title", "Update Product");
		List<Category> categories = categoryService.findAllByActivated();
		ProductDto productDto = productService.getById(id);
		model.addAttribute("categories", categories);
		model.addAttribute("productDto", productDto);
		
		return "update-product";
	}
	
	@PostMapping("/update-product/{id}")
	public String update(@ModelAttribute("productDto")ProductDto productDto,
			@PathVariable("id")Long id,
			@RequestParam("imageProduct")MultipartFile imageProduct,
			RedirectAttributes attributes) {
		
		try {
			productService.update(imageProduct, productDto);
			attributes.addFlashAttribute("success", "Update successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Can't not update product!");
			
		}
		
		return "redirect:/products/0";
		
	}
	
	
	@RequestMapping(value = "/enabled-product/{id}", method = {RequestMethod.GET, RequestMethod.PUT})
	public String enableProduct(@PathVariable("id")Long id, RedirectAttributes attributes) {
		try {
			productService.enableById(id);
			attributes.addFlashAttribute("success", "Enabled successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Can't not enable!");
		}
		return "redirect:/products/0";
	}
	
	@RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.GET, RequestMethod.PUT})
	public String deleteProduct(@PathVariable("id")Long id, RedirectAttributes attributes) {
		try {
			productService.deleteById(id);
			attributes.addFlashAttribute("success", "Delete successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Can't not delete!");
		}
		return "redirect:/products/0";
	}
	
	@GetMapping("/products/{pageNo}")
	public String productsPage(@PathVariable("pageNo")int pageNo, Model model, Principal principal) {
		if(principal == null) {
			return "redirect:/login";
		}
		Page<ProductDto> products = productService.pageProducts(pageNo);
		model.addAttribute("title", "List Product");
		model.addAttribute("size", products.getSize());
		model.addAttribute("totalPages", products.getTotalPages());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("products", products);
		return "products";
	}
	@GetMapping("/search-result/{pageNo}")
	public String searchProducts(@RequestParam("keyword")String keyword,
			@PathVariable("pageNo")int pageNo, Model model, Principal principal) {
		if(principal == null) {
			return "redirect:/login";
		}
		Page<ProductDto> products = productService.searchProducts(keyword,pageNo);
		model.addAttribute("title", "Search Result");
		model.addAttribute("keyword", keyword);
		model.addAttribute("size", products.getSize());
		model.addAttribute("totalPages", products.getTotalPages());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("products", products);
		
		return "result-product";
	}
	
}
