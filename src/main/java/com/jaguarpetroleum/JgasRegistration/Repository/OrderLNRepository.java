package com.jaguarpetroleum.JgasRegistration.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.OrderLN;

@Repository
public interface OrderLNRepository extends JpaRepository<OrderLN, Integer> {
	@Query(value = "SELECT * FROM tb_OrderLN WHERE orderNo = :orderNo", nativeQuery = true)
	public List<OrderLN> findByOrderNo(@Param("orderNo") String orderNo);
	
	@Modifying 
	@Query(value = "UPDATE tb_OrderLN SET refilledCylinderSerial = :refilledCylinderSerial, weight = :weight, serialNo = :serialNo "
			+ "WHERE orderNo = :orderNo AND productId = :productId", nativeQuery = true)
	int updateScanDetails(@Param("refilledCylinderSerial") String refilledCylinderSerial, 
			@Param("weight") String weight, 
			@Param("serialNo") String serialNo, 
			@Param("orderNo") String orderNo, 
			@Param("productId") String productId);
}
