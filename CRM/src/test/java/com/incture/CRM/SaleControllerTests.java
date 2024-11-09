package com.incture.CRM;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.incture.CRM.Controller.SaleController;
import com.incture.CRM.Entity.Sale;
import com.incture.CRM.Service.SaleService;

class SaleControllerTests {

    @InjectMocks
    private SaleController saleController;

    @Mock
    private SaleService saleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Test to create sale
    @Test
    void testCreateSales() {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setProduct("Product1");
        sale.setStage("Lead");
        sale.setDealSize(100.0);

        when(saleService.saveSale(sale)).thenReturn(sale);

        ResponseEntity<?> response = saleController.createSales(sale);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sale, response.getBody());

        verify(saleService, times(1)).saveSale(sale);
    }

    //Test to get sale by it's id when sale exists
    @Test
    void testGetSalesById() {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setProduct("Product1");

        when(saleService.getSaleById(1L)).thenReturn(Optional.of(sale));

        ResponseEntity<?> response = saleController.getSalesById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sale, response.getBody());

        verify(saleService, times(1)).getSaleById(1L);
    }

    //Test to get sale by id when it doesn't exist
    @Test
    void testGetSalesById_NotFound() {
        when(saleService.getSaleById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = saleController.getSalesById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(saleService, times(1)).getSaleById(1L);
    }

    //Test to get all sales
    @Test
    void testGetAllSales() {
        Sale sale1 = new Sale(1L, "Product1", "Lead", 100.0, null, null);
        Sale sale2 = new Sale(2L, "Product2", "Closed", 200.0, null, null);
        
        when(saleService.getAllSales()).thenReturn(Arrays.asList(sale1, sale2));

        ResponseEntity<?> response = saleController.getAllSales();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(sale1, sale2), response.getBody());

        verify(saleService, times(1)).getAllSales();
    }

    //Test to update a sale when a sale exists
    @Test
    void testUpdateSale() {
        Sale existingSale = new Sale(1L, "Product1", "Lead", 100.0, null, null);
        Sale updatedSale = new Sale(1L, "UpdatedProduct", "Contact Made", 150.0, null, null);

        when(saleService.getSaleById(1L)).thenReturn(Optional.of(existingSale));
        when(saleService.saveSale(any(Sale.class))).thenReturn(updatedSale);

        ResponseEntity<String> response = saleController.updateSale(1L, updatedSale);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sale updated successfully", response.getBody());

        verify(saleService, times(1)).getSaleById(1L);
        verify(saleService, times(1)).saveSale(any(Sale.class));
    }

    //Test to update a sale when it doesn't exist
    @Test
    void testUpdateSale_NotFound() {
        Sale updatedSale = new Sale(1L, "UpdatedProduct", "Contact Made", 150.0, null, null);

        when(saleService.getSaleById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = saleController.updateSale(1L, updatedSale);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Sale not found", response.getBody());

        verify(saleService, times(1)).getSaleById(1L);
        verify(saleService, times(0)).saveSale(any(Sale.class));
    }

    //Test to delete a sale when it exists
    @Test
    void testDeleteSale() {
        when(saleService.saleExistsById(1L)).thenReturn(true);
        doNothing().when(saleService).deleteSaleById(1L);

        ResponseEntity<String> response = saleController.deleteSale(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sale and associated logs deleted successfully", response.getBody());

        verify(saleService, times(1)).saleExistsById(1L);
        verify(saleService, times(1)).deleteSaleById(1L);
    }

    //Test to delete a sale when it doesn't exist
    @Test
    void testDeleteSale_NotFound() {
        when(saleService.saleExistsById(1L)).thenReturn(false);

        ResponseEntity<String> response = saleController.deleteSale(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Sale not found", response.getBody());

        verify(saleService, times(1)).saleExistsById(1L);
        verify(saleService, times(0)).deleteSaleById(1L);
    }

    //Test to get count of sales in each stage
    @Test
    void testGetSalesStageCount() {
        // Expected result map
        Map<String, Long> expectedStageCountMap = new HashMap<>();
        expectedStageCountMap.put("Lead", 10L);
        expectedStageCountMap.put("Contact Made", 0L);
        expectedStageCountMap.put("Qualification", 0L);
        expectedStageCountMap.put("Demo Scheduled", 0L);
        expectedStageCountMap.put("Closed", 5L);

        // Mock the service layer to return specific counts for each stage
        when(saleService.countByStage("Lead")).thenReturn(10L);
        when(saleService.countByStage("Contact Made")).thenReturn(0L);
        when(saleService.countByStage("Qualification")).thenReturn(0L);
        when(saleService.countByStage("Demo Scheduled")).thenReturn(0L);
        when(saleService.countByStage("Closed")).thenReturn(5L);

        // Call the controller method
        ResponseEntity<?> response = saleController.getSalesStageCount();

        // Assert the status code and the returned map
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStageCountMap, response.getBody());

        // Verify that the service layer methods were called with the expected stage names
        verify(saleService, times(1)).countByStage("Lead");
        verify(saleService, times(1)).countByStage("Contact Made");
        verify(saleService, times(1)).countByStage("Qualification");
        verify(saleService, times(1)).countByStage("Demo Scheduled");
        verify(saleService, times(1)).countByStage("Closed");
    }

    //Test to get total deal size for closed sales
    @Test
    void testGetTotalDealSizeForClosedSales() {
        when(saleService.getTotalDealSizeForClosedSales()).thenReturn(250.0);

        ResponseEntity<?> response = saleController.getTotalDealSizeForClosedSales();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(250.0, response.getBody());

        verify(saleService, times(1)).getTotalDealSizeForClosedSales();
    }

    //Test to get total deal size for unclosed sales
    @Test
    void testGetTotalDealSizeForUnclosedSales() {
        when(saleService.getTotalDealSizeForUnclosedSales()).thenReturn(125.0);

        ResponseEntity<?> response = saleController.getTotalDealSizeForUnclosedSales();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(125.0, response.getBody());

        verify(saleService, times(1)).getTotalDealSizeForUnclosedSales();
    }
}
