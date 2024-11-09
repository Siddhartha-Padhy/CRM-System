package com.incture.CRM.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.CRM.Entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
	// Count sales in each stage
	Long countByStage(String stage);
}