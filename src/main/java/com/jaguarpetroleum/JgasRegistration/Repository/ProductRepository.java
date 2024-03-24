package com.jaguarpetroleum.JgasRegistration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
	@Query(value = "SELECT * FROM tb_Product WHERE productId = :productId", nativeQuery = true)
	public Product findByProductId(@Param("productId") String productId);
	
	@Query(value = "SELECT * FROM tb_Product WHERE productName = :productName", nativeQuery = true)
	public Product findByProductName(@Param("productName") String productName);
}
