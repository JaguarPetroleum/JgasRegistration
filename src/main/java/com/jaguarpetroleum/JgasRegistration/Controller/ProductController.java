package com.jaguarpetroleum.JgasRegistration.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jaguarpetroleum.JgasRegistration.Model.Product;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private com.jaguarpetroleum.JgasRegistration.Service.ProductService productService;
	
	@PostMapping("/createProduct")
	public JSONObject add(@RequestBody JSONObject productDetails) {
		logger.info("Received Product details "+productDetails);
		JSONObject response = new JSONObject();
		
		Product product = new Product();
		
		try {
			product.setProductDescription(productDetails.get("productDescription").toString()); 
			product.setProductId(productDetails.get("productId").toString());
			product.setProductImagePath(productDetails.get("productImagePath").toString());
			product.setProductName(productDetails.get("productName").toString());
			
			productService.save(product);
			logger.info("New product has been created");
			response.put("resultCode", 0);
			response.put("resultMessage", "Product has been successfully created");
		} catch (Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "An error occured while creating product. "+e.getMessage());
		}
		
		return response;		
	}
	
	@PutMapping("/updateProduct")
	public JSONObject updateProduct(@RequestBody JSONObject productDetails) {
		logger.info("Received Product details to update "+productDetails);
		JSONObject response = new JSONObject();
		
		Product product = new Product();
		
		try {
			if( productService.findByProductId(productDetails.get("productId").toString()) != null) {
				product.setProductDescription(productDetails.get("productDescription").toString()); 
				product.setProductId(productDetails.get("productId").toString());
				product.setProductImagePath(productDetails.get("productImagePath").toString());
				product.setProductName(productDetails.get("productName").toString());
				
				productService.save(product);
				logger.info("Product has been updated");
				response.put("resultCode", 0);
				response.put("resultMessage", "Product has been successfully updated");
			} else {
				response.put("resultCode", 20010);
				response.put("resultMessage", "No Product exists with the productId "+productDetails.get("productId").toString());
			}
			
		} catch (Exception e) {
			response.put("resultCode", 10010);
			response.put("resultMessage", "An error occured while updating product. "+e.getMessage());
		}
		
		return response;		
	}
	
	@GetMapping("/allProducts")
	public List<Product> getAllProducts(){
		try {
			return productService.listAll();

		} catch(Exception e) {
			return null;
		}		
	}
	
	@GetMapping("/productById/{productId}")
	public Product productById(@PathVariable String productId){
		logger.info("Received request for product details "+ productId);		
		try {
			return productService.findByProductId(productId);
		} catch(Exception e) {
			logger.error("Error encountered while retrieving product details "+e.getMessage());
			return null;
		}				
	}
}
