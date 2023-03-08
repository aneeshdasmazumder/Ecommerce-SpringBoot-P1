package com.project1.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project1.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
