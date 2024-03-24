package com.jaguarpetroleum.JgasRegistration.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaguarpetroleum.JgasRegistration.Model.Location;
import com.jaguarpetroleum.JgasRegistration.Model.Product;
import com.jaguarpetroleum.JgasRegistration.Repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	public Product findByProductId(String productId) {
		return productRepository.findByProductId(productId);
	}
	
	public Product findByProductName(String productName) {
		return productRepository.findByProductName(productName);
	}
	
	public void save(Product product) {
		productRepository.save(product);
	}
	
	public List<Product> listAll(){
		return productRepository.findAll();
	}
}
