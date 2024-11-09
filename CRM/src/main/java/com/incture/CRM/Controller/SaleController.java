package com.incture.CRM.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.CRM.Entity.Sale;
import com.incture.CRM.Service.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
	private final SaleService saleService;

	private static final Logger LOGGER = LoggerFactory.getLogger(SaleController.class);
    
    @Autowired
    public SaleController(SaleService salesService) {
        this.saleService = salesService;
    }

    // Add a new sale
    @PostMapping("/addSale")
    public ResponseEntity<?> createSales(@RequestBody Sale sale) {
        try {
            Sale savedSale = saleService.saveSale(sale);
            LOGGER.info("Sale created successfully with ID: {}", savedSale.getId());
            return ResponseEntity.ok(savedSale);
        } catch (Exception e) {
            LOGGER.error("Failed to create sale: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create sale. Please try again.");
        }
    }
    
    // Get a sale by id
    @GetMapping("/sales/{id}")
    public ResponseEntity<?> getSalesById(@PathVariable Long id) {
    	try {
    		return saleService.getSaleById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    	}catch(Exception e) {
    		LOGGER.error("Error fetching sale with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch sale. Please try again.");
    	}
    }

    // Get all the sales
    @GetMapping("/allSales")
    public ResponseEntity<?> getAllSales() {
        try {
            List<Sale> sales = saleService.getAllSales();
            LOGGER.info("Fetched all sales, total count: {}", sales.size());
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch all sales: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch all sales. Please try again.");
        }
    }
    
    // Update a sale entry
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSale(@PathVariable Long id, @RequestBody Sale updatedSaleData) {
        try {
            return saleService.getSaleById(id)
                    .map(existingSale -> {
                        existingSale.setProduct(updatedSaleData.getProduct());
                        existingSale.setStage(updatedSaleData.getStage());
                        existingSale.setDealSize(updatedSaleData.getDealSize());
                        existingSale.setClosingDate(updatedSaleData.getClosingDate());

                        saleService.saveSale(existingSale); // Save the updated sale data
                        LOGGER.info("Sale with ID: {} updated successfully", id);
                        return new ResponseEntity<>("Sale updated successfully", HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        LOGGER.warn("Sale with ID: {} not found for update", id);
                        return new ResponseEntity<>("Sale not found", HttpStatus.NOT_FOUND);
                    });
        } catch (Exception e) {
            LOGGER.error("Failed to update sale with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>("Failed to update sale. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Count no. of sales in each stage
    @GetMapping("/salesStageCount")
    public ResponseEntity<?> getSalesStageCount() {
        try {
            Map<String, Long> stageCountMap = new HashMap<>();
            stageCountMap.put("Lead", saleService.countByStage("Lead"));
            stageCountMap.put("Contact Made", saleService.countByStage("Contact Made"));
            stageCountMap.put("Qualification", saleService.countByStage("Qualification"));
            stageCountMap.put("Demo Scheduled", saleService.countByStage("Demo Scheduled"));
            stageCountMap.put("Closed", saleService.countByStage("Closed"));
            LOGGER.info("Fetched sales stage counts");
            return ResponseEntity.ok(stageCountMap);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch sales stage counts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch sales stage counts. Please try again.");
        }
    }
    
    // Delete a sale and all the customer interaction logs associated with it.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable Long id) {
        try {
            if (saleService.saleExistsById(id)) {
                saleService.deleteSaleById(id);
                LOGGER.info("Sale with ID: {} and associated logs deleted successfully", id);
                return new ResponseEntity<>("Sale and associated logs deleted successfully", HttpStatus.OK);
            } else {
                LOGGER.warn("Sale with ID: {} not found for deletion", id);
                return new ResponseEntity<>("Sale not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete sale with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>("Failed to delete sale. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get total deal size for closed sales(current revenue)
    @GetMapping("/totalDealSizeForClosedSales")
    public ResponseEntity<?> getTotalDealSizeForClosedSales() {
        try {
            double totalDealSize = saleService.getTotalDealSizeForClosedSales();
            LOGGER.info("Total deal size for closed sales fetched: {}", totalDealSize);
            return ResponseEntity.ok(totalDealSize);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch total deal size for closed sales: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch total deal size for closed sales. Please try again.");
        }
    }
    
    // Get total deal size for unclosed sales(expected revenue)
    @GetMapping("/totalDealSizeForUnclosedSales")
    public ResponseEntity<?> getTotalDealSizeForUnclosedSales() {
        try {
            double totalDealSize = saleService.getTotalDealSizeForUnclosedSales();
            LOGGER.info("Total deal size for unclosed sales fetched: {}", totalDealSize);
            return ResponseEntity.ok(totalDealSize);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch total deal size for unclosed sales: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch total deal size for unclosed sales. Please try again.");
        }
    }
    
}
