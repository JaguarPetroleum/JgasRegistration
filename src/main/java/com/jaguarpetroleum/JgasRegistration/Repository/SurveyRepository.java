package com.jaguarpetroleum.JgasRegistration.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaguarpetroleum.JgasRegistration.Model.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Integer> {

	@Query(value = "SELECT * FROM tb_Survey WHERE orderNo = :orderNo", nativeQuery = true)
	public List<Survey> findByOrderNo(@Param("orderNo") String orderNo);
}
