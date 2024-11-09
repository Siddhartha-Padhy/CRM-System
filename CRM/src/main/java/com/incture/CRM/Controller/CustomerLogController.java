package com.incture.CRM.Controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incture.CRM.Entity.CustomerLog;
import com.incture.CRM.Entity.Sale;
import com.incture.CRM.Service.CustomerLogService;
import com.incture.CRM.Service.SaleService;

@RestController
@RequestMapping("/api/customerLogs")
public class CustomerLogController {
    private final CustomerLogService customerLogService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerLogController.class);
    
    @Autowired
    private SaleService saleService;

    @Autowired
    public CustomerLogController(SaleService saleService, CustomerLogService customerLogService) {
        this.saleService = saleService;
        this.customerLogService = customerLogService;
    }

    // Add a customer interaction log for a specific sale
    @PostMapping("/addLog")
    public ResponseEntity<?> addCustomerLog(@RequestParam Long saleId, @RequestBody CustomerLog customerLog) {
        try {
            // Find the Sale by its ID
            Sale sale = saleService.getSaleById(saleId)
                    .orElseThrow(() -> new RuntimeException("Sale not found"));

            customerLog.setSale(sale);

            // Save the CustomerLog in the database
            CustomerLog savedLog = customerLogService.saveCustomerLog(customerLog);
            LOGGER.info("Customer log added successfully for saleId: {}", saleId);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedLog);

        } catch (Exception e) {
        	LOGGER.error("Error adding customer log for saleId {}: {}", saleId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add customer log. Please try again.");
        }
    }
    
    //Fetch all customer logs for a sale by sale id
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<?> getCustomerLogsBySaleId(@PathVariable Long saleId) {
        try {
            List<CustomerLog> logs = customerLogService.getCustomerLogsBySaleId(saleId);
            LOGGER.info("Fetched {} logs for saleId: {}", logs.size(), saleId);

            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            LOGGER.error("Error fetching customer logs for saleId {}: {}", saleId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch customer logs. Please try again.");
        }
    }

    //Fetch a customer log using the log id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerLogById(@PathVariable Long id) {
    	try {
    		return customerLogService.getCustomerLogById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    	}catch(Exception e){
    		LOGGER.error("Error fetching customer log with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch customer log. Please try again.");
    	}
        
    }
    
    // Fetch count of customer logs for each customer
    @GetMapping("/countByCustomer")
    public ResponseEntity<?> getCountLogsByCustomer() {
        try {
            Map<String, Long> counts = customerLogService.countLogsByCustomer();
            LOGGER.info("Fetched log counts for each customer");

            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            LOGGER.error("Error fetching log counts by customer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch log counts. Please try again.");
        }
    }
}
