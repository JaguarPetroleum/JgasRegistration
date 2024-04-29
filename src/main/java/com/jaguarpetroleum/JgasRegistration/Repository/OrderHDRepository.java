package com.jaguarpetroleum.JgasRegistration.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.OrderHD;

@Repository
public interface OrderHDRepository  extends JpaRepository<OrderHD, Integer>{
	@Query(value = "SELECT * FROM tb_OrderHD WHERE orderNo = :orderNo", nativeQuery = true)
	public OrderHD findByOrderNo(@Param("orderNo") String orderNo);
	
	@Query(value = "SELECT * FROM tb_OrderHD WHERE deliveryCode = :deliveryCode", nativeQuery = true)
	public OrderHD findByDeliveryCode(@Param("deliveryCode") String deliveryCode);
	
	@Query(value = "SELECT * FROM tb_OrderHD WHERE locationId = :locationId", nativeQuery = true)
	public List<OrderHD> findByLocationId(@Param("locationId") String locationId);
	
	@Query(value = "SELECT t1.*, t2.firstName+' '+t2.lastName as customerName, t2.phoneNumber as customerNumber FROM tb_OrderHD t1 inner join tb_Registration t2 on t1.phoneNumber = t2.phoneNumber WHERE locationId = :locationId", nativeQuery = true)
	public List<OrderHD> findByLocationId2(@Param("locationId") String locationId);
	
	@Query(value = "SELECT * FROM tb_OrderHD WHERE phoneNumber = :phoneNumber", nativeQuery = true)
	public List<OrderHD> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	@Modifying 
	@Query(value = "UPDATE tb_OrderHD SET checkOutRequestId = :checkOutRequestId WHERE orderNo = :orderNo", nativeQuery = true)
	public void updateCheckout(@Param("checkOutRequestId") String checkOutRequestId, @Param("orderNo") String orderNo);
	
	@Modifying 
	@Query(value = "UPDATE tb_OrderHD SET deliveryCode = :deliveryCode WHERE orderNo = :orderNo", nativeQuery = true)
	public void updateDeliveryCode(@Param("deliveryCode") String deliveryCode, @Param("orderNo") String orderNo);
	
	@Modifying 
	@Query(value = "UPDATE tb_OrderHD SET recepientName = :recepientName, recepientPhone = :recepientPhone WHERE orderNo = :orderNo", nativeQuery = true)
	public void updateRecepient(@Param("recepientName") String recepientName, @Param("recepientPhone") String recepientPhone, @Param("orderNo") String orderNo);
	
	@Modifying 
	@Query(value = "UPDATE tb_OrderHD SET status = :status WHERE orderNo = :orderNo", nativeQuery = true)
	public void updateStatus(@Param("status") String status, @Param("orderNo") String orderNo);

	@Modifying 
	@Query(value = "UPDATE tb_OrderHD SET paid = :resultCode, mpesaDescription = :resultDesc,  status = :status, transactionRef = :transactionRef "
			+ "WHERE checkOutRequestId = :checkOutRequestId", nativeQuery = true)
	public void updatePaymentDetails(@Param("checkOutRequestId") String checkOutRequestId, 
			@Param("transactionRef") String transactionRef, 
			@Param("resultCode") int resultCode, 
			@Param("resultDesc") String resultDesc, 
			@Param("status") String status);

	@Query(value = "SELECT * FROM tb_OrderHD WHERE orderNo = :orderNo AND totalCost = :amount", nativeQuery = true)
	public OrderHD findByOrderNoAndAmount(@Param("orderNo") String orderNo, @Param("amount") Double amount);

	@Modifying 
	@Query(value = "UPDATE tb_OrderHD SET paid = :resultCode, mpesaDescription = :resultDesc,  status = :status, transactionRef = :transactionRef "
			+ "WHERE orderNo = :orderNo", nativeQuery = true)
	public void updatePaymentDetailsC2B(@Param("orderNo") String orderNo, @Param("transactionRef") String transactionRef, @Param("resultCode") int resultCode, 
			@Param("resultDesc") String resultDesc, @Param("status") String status);
	
	@Modifying 
	@Query(value = "UPDATE tb_OrderHD SET status = 'System Cancelled' WHERE status = 'Order Placed' AND TIMEDIFF(TIME(orderDatetime), TIME(now())) > '01:00:00'", nativeQuery = true)
	public void cancelUnpaidOrders();

	@Query(value = "SELECT * FROM tb_OrderHD WHERE checkOutRequestId = :checkOutRequestId", nativeQuery = true)
	public OrderHD findByCheckOutId(String checkOutRequestId);
	
	
	public interface OrdersPerDay {
		String getOrderDate();
		Integer getOrdersCount();
	}
	
	@Query(value = "select DATE_FORMAT(orderDatetime, %m-%d) as orderDate,count(*) as ordersCount from tb_OrderHD where date(orderDatetime) >='?1' group by orderDate;",nativeQuery = true)
    List<OrdersPerDay> findOrdersCountPerDay(String dateFrom);
	
	@Query(value = "SELECT COUNT(*) FROM tb_OrderHD WHERE customerNumber = :phoneNumber AND MONTH(recordDatetime) = MONTH(current_date()) AND YEAR(recordDatetime) = YEAR(current_date());", nativeQuery = true)
	public Integer staffDiscountTimes(String phoneNumber);
}
