package com.project1.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project1.productservice.dto.ProductRequest;
import com.project1.productservice.dto.ProductResponse;
import com.project1.productservice.model.Product;
import com.project1.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepo;
	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		productRepo.save(product);
		
		log.info("product {} is saved.",product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepo.findAll();
		
		return products.stream().map(product -> mapToProductResponse(product)).toList();
		
	}

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}
	
}
