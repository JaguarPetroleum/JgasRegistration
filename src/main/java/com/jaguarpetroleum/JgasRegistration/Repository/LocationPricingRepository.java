package com.jaguarpetroleum.JgasRegistration.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.LocationPricing;

@Repository
public interface LocationPricingRepository extends JpaRepository<LocationPricing, Integer>{
	@Query(value = "SELECT * FROM tb_LocationPricing WHERE productId = :productId AND locationId = :locationId limit 1", nativeQuery = true)
	public LocationPricing findByProductIdandLocationId(@Param("productId") String productId, @Param("locationId") String locationId);
	
	@Query(value = "SELECT t1.locationId, t1.productId, if(t2.isGas = 1, t1.price - :staffDiscount, t1.price) as price, t1.recordNumber "
			+ "FROM tb_LocationPricing t1 INNER JOIN tb_Product t2 "
			+ "ON t1.productId = t2.productId  "
			+ "WHERE t1.locationId = :locationId", nativeQuery = true)
	public List<LocationPricing> findByLocationIdforStaff(@Param("locationId") String locationId, @Param("staffDiscount") Double staffDiscount);

	@Query(value = "SELECT * FROM tb_LocationPricing WHERE locationId = :locationId", nativeQuery = true)
	public List<LocationPricing> findByLocationId(@Param("locationId") String locationId);

	@Modifying 
	@Query(value = "UPDATE tb_LocationPricing SET price = :price WHERE locationId = :locationId AND productId = :productId", nativeQuery = true)
	public void updatePricing(@Param("price") Double price, @Param("locationId") String locationId, @Param("productId") String productId);
}
