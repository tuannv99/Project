package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;

public interface CategoryService {
	List<Category> findAll();
	
	Category save(Category category);
	
	Category getById(Long id);
	
	Category update(Category category);
	
	void deleteById(Long id);
	
	void enableById(Long id);
	
	Category findByName(String name);
	
	List<Category> findAllByActivated();
	
	/**/
	List<CategoryDto>getCategoryAndProduct();
	
	
}
