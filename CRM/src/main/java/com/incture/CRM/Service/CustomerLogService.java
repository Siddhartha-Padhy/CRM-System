package com.incture.CRM.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.CRM.Entity.CustomerLog;
import com.incture.CRM.Repository.CustomerLogRepository;

@Service
public class CustomerLogService {
    private final CustomerLogRepository customerLogRepository;
    private static final Logger LOGGER=LoggerFactory.getLogger(CustomerLogService.class);
    
    @Autowired
    public CustomerLogService(CustomerLogRepository customerRepository) {
        this.customerLogRepository = customerRepository;
    }

    //save customer log
    public CustomerLog saveCustomerLog(CustomerLog customer) {
    	LOGGER.info("Adding new customer log");
        return customerLogRepository.save(customer);
    }

    //get customer log by id
    public Optional<CustomerLog> getCustomerLogById(Long id) {
    	LOGGER.info("Fetching customer log with id="+id);
        return customerLogRepository.findById(id);
    }
    
    //count the no. of logs for each customer
    public Map<String, Long> countLogsByCustomer() {
    	Map<String, Long> logCountByCustomer = new HashMap<>();
    	try {
    		List<CustomerLog> logs = customerLogRepository.findAll();	//get all the logs

    		//loop through the logs and set the customer and it's log count
            for (CustomerLog log : logs) {
                String customerName = log.getCustomer();
                logCountByCustomer.put(customerName, logCountByCustomer.getOrDefault(customerName, 0L) + 1);
            }
    	}catch(Exception e) {
    		LOGGER.warn(""+e);
    	}
    	return logCountByCustomer;
    }
    
    //Fetch all customer logs for a particular sale id
    public List<CustomerLog> getCustomerLogsBySaleId(Long saleId) {
    	LOGGER.info("Fetching customer logs for sale id="+saleId);
        return customerLogRepository.findBySaleId(saleId);
    }
}
