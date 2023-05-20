package com.ecommerce.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	
		
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category save(Category category) {
		try {
			Category categorySave = new Category(category.getName());
			return categoryRepository.save(categorySave);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Category getById(Long id) {
		
		return categoryRepository.findById(id).get();
	}

	@Override
	public Category update(Category category) {
		Category categoryUpdate = categoryRepository.findById(category.getId()).get();
		categoryUpdate.setName(category.getName());
		categoryUpdate.set_activated(category.is_activated());
		categoryUpdate.set_deleted(category.is_deleted());
		return categoryRepository.save(categoryUpdate);
	}

	@Override
	public void deleteById(Long id) {
		Category category = categoryRepository.findById(id).get();
		
		category.set_deleted(true);
		category.set_activated(false);
		categoryRepository.save(category);
		
	}

	@Override
	public void enableById(Long id) {
		Category category = categoryRepository.findById(id).get();
		
		category.set_deleted(false);
		category.set_activated(true);
		categoryRepository.save(category);
		
		
	}

	@Override
	public Category findByName(String name) {
		
		return categoryRepository.findByName(name);
	}

	@Override
	public List<Category> findAllByActivated() {
		return categoryRepository.findAllByActivated();
	}

	@Override
	public List<CategoryDto> getCategoryAndProduct() {

		return categoryRepository.getCategoryAndProduct();
	}

}
