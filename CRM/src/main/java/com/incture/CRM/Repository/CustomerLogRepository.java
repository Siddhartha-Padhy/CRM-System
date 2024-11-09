package com.incture.CRM.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.CRM.Entity.CustomerLog;

public interface CustomerLogRepository extends JpaRepository<CustomerLog, Long> {
	//function to find all customer logs by a sale id
	List<CustomerLog> findBySaleId(Long saleId);
}