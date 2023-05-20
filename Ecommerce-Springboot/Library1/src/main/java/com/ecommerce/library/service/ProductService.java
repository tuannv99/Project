package com.ecommerce.library.service;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;

public interface ProductService {
	/*Admin*/
	List<ProductDto> findAll();
	
	Product save(MultipartFile imageProduct, ProductDto productDto);
	Product update(MultipartFile imageProduct,ProductDto productDto);
	ProductDto getById(Long id);
	
	void deleteById(Long id);
	void enableById(Long id);
	
	Page<ProductDto> pageProducts(int pageNo);
	
	Page<ProductDto>searchProducts(String keyword, int pageNo);
	
	/*Customer*/
	
	List<Product> getAllProducts();
	List<Product> listViewProducts();
	
	Product getProductById(Long id);
	
	List<Product> getRelatedProduct(Long categoryId);
	
	List<Product> getProductsInCategory(Long categoryId);
	
	List<Product>filterHighToLow();
	List<Product>filterLowToHigh();
}
