package com.incture.CRM.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.incture.CRM.Entity.Sale;
import com.incture.CRM.Repository.SaleRepository;

@Service
public class SaleService {
	private final SaleRepository saleRepository;
	private static final Logger LOGGER=LoggerFactory.getLogger(SaleService.class);
    
    @Autowired
    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    //Save a sale
    public Sale saveSale(Sale sale) {
    	LOGGER.info("Adding a new sale");
        return saleRepository.save(sale);
    }

    //Get sale by id
    public Optional<Sale> getSaleById(Long id) {
    	LOGGER.info("Fetching sale with id="+id);
        return saleRepository.findById(id);
    }

    //Get all sales
    public List<Sale> getAllSales() {
    	LOGGER.info("Fetching all sales");
        return saleRepository.findAll();
    }
    
    //Check if sale exists by sale id
    public boolean saleExistsById(Long id) {
    	return saleRepository.existsById(id);
    }
    
    //Count no. of sales in each stage
    public Long countByStage(String stage) {
    	return saleRepository.countByStage(stage);
    }
    
    //Delete a sale by sale id
    public void deleteSaleById(Long id) {
    	LOGGER.warn("Deleting sale with id="+id);
    	saleRepository.deleteById(id);
    }
    
    //Get total deal size for closed sales(current revenue)
    public double getTotalDealSizeForClosedSales() {
    	LOGGER.info("Fetching total deal size for closed sales");
        List<Sale> sales = saleRepository.findAll();
        double totalDealSize = 0.0;
        for (Sale sale : sales) {
            if ("Closed".equalsIgnoreCase(sale.getStage())) {
                totalDealSize += sale.getDealSize();
            }
        }
        return totalDealSize;
    }
    
    //Get total deal size for unclosed sales(expected revenue)
    public double getTotalDealSizeForUnclosedSales() {
    	LOGGER.info("Fetching total deal size for unclosed sales");
        List<Sale> sales = saleRepository.findAll();
        double totalDealSize = 0.0;
        for (Sale sale : sales) {
            if ("Closed".equalsIgnoreCase(sale.getStage())) {
                continue;
            }
            else {
            	totalDealSize += sale.getDealSize();
            }
        }
        return totalDealSize;
    }
}
