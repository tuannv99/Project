package com.ecommerce.library.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageUpload imageUpload;


	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<ProductDto> findAll() {
		
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDtoList =  transfer(products);
		
		return productDtoList;
	}

	@Override
	public Product save(MultipartFile imageProduct,  ProductDto productDto) {
		try {
			Product product = new Product();
			if(imageProduct == null) {
				product.setImage(null);
			}else {
				imageUpload.uploadImage(imageProduct);
				product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
			}
			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setCategory(productDto.getCategory());
			product.setCostPrice(productDto.getCostPrice());
			product.setCurrentQuantity(productDto.getCurrentQuantity());
			product.set_activated(true);
			product.set_deleted(false);
			return productRepository.save(product);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Product update(MultipartFile imageProduct, ProductDto productDto) {
		try {
			Product product = productRepository.findById(productDto.getId()).get();
			if(imageProduct == null) {
				product.setImage(product.getImage());
			}else {
				if(imageUpload.checkExsited(imageProduct) == false) {
					imageUpload.uploadImage(imageProduct);
					product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
				}
			}
			product.setId(productDto.getId());
			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setSalePrice(productDto.getSalePrice());
			product.setCostPrice(productDto.getCostPrice());
			product.setCurrentQuantity(productDto.getCurrentQuantity());
			product.setCategory(productDto.getCategory());
			return productRepository.save(product);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public void deleteById(Long id) {
		Product product = productRepository.findById(id).get();
		product.set_activated(false);
		product.set_deleted(true);
		productRepository.save(product);
	
	}

	@Override
	public void enableById(Long id) {
		Product product = productRepository.findById(id).get();
		product.set_activated(true);
		product.set_deleted(false);
		productRepository.save(product);
		
	}
	
	@Override
	public ProductDto getById(Long id) {
		Product product = productRepository.findById(id).get();
		ProductDto productDto = new ProductDto();
		productDto.setId(product.getId());
		productDto.setName(product.getName());
		productDto.setDescription(product.getDescription());
		productDto.setCurrentQuantity(product.getCurrentQuantity());
		productDto.setCategory(product.getCategory());
		productDto.setSalePrice(product.getSalePrice());
		productDto.setCostPrice(product.getCostPrice());
		productDto.setImage(product.getImage());
		productDto.setDeleted(product.is_deleted());
		productDto.setActivated(product.is_activated());
		return productDto;
	}
	

	
//	private Product transferDTO(ProductDto productDto) {
//		Product product = new Product();
//		product.setId(productDto.getId());
//		product.setName(productDto.getName());
//		product.setDescription(productDto.getDescription());
//		product.setSalePrice(productDto.getSalePrice());
//		product.setCostPrice(productDto.getCostPrice());
//		product.setCategory(productDto.getCategory());
//		product.setCurrentQuantity(productDto.getCurrentQuantity());
//		product.set_activated(productDto.isActivated());
//		product.set_deleted(productDto.isDeleted());
//		product.setImage(productDto.getImage());
//		return product;
//	}

	@Override
	public Page<ProductDto> pageProducts(int pageNo) {
		Pageable pageable = PageRequest.of(pageNo, 3);
		List<ProductDto> products = transfer(productRepository.findAll());
		Page<ProductDto> productPages = toPage(products, pageable);
		return productPages;
	}

	@Override
	public Page<ProductDto> searchProducts(String keyword, int pageNo) {
		Pageable pageable = PageRequest.of(pageNo, 3);
		List<ProductDto>productDtoList = transfer(productRepository.searchProductsList(keyword));
		Page<ProductDto> products = toPage(productDtoList, pageable);
		return products;
	}
	
	private Page toPage(List<ProductDto> list, Pageable pageable) {
		if(pageable.getOffset() >= list.size()) {
			return Page.empty();
		}
		System.out.println( "page.offset = " + pageable.getOffset());
		System.out.println(list.size());
		int startIndex = (int) pageable.getOffset();
		int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > list.size())
				? list.size()
				: pageable.getOffset() + pageable.getPageSize());
		
		List subList = list.subList(startIndex, endIndex);
		return new PageImpl<>(subList, pageable, list.size());
	}
	
	private List<ProductDto> transfer(List<Product> products) {
		List<ProductDto> productDtoList = new ArrayList<>();
		for (Product product : products) {
			ProductDto productDto = new ProductDto();
			productDto.setId(product.getId());
			productDto.setName(product.getName());
			productDto.setDescription(product.getDescription());
			productDto.setSalePrice(product.getSalePrice());
			productDto.setCostPrice(product.getCostPrice());
			productDto.setCategory(product.getCategory());
			productDto.setCurrentQuantity(product.getCurrentQuantity());
			productDto.setActivated(product.is_activated());
			productDto.setDeleted(product.is_deleted());
			productDto.setImage(product.getImage());
			productDtoList.add(productDto);
		}
		
		return productDtoList;
		
	}
	
	/*Customer*/

	@Override
	public List<Product> getAllProducts() {

		return productRepository.getAllProducts();
	}

	@Override
	public List<Product> listViewProducts() {

		return productRepository.listViewProducts();
	}

	@Override
	public Product getProductById(Long id) {

		return productRepository.findById(id).get();
	}

	@Override
	public List<Product> getRelatedProduct(Long categoryId) {

		return productRepository.getRelatedProducts(categoryId);
	}

	@Override
	public List<Product> getProductsInCategory(Long categoryId) {
		return productRepository.getProductsInCategory(categoryId);
	}

	@Override
	public List<Product> filterHighToLow() {

		return productRepository.filterHighPriceToLow();
	}

	@Override
	public List<Product> filterLowToHigh() {

		return productRepository.filterLowPriceToHight();
	}
	



}
