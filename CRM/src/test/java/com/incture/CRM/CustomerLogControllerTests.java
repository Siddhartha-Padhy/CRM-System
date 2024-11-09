package com.incture.CRM;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import com.incture.CRM.Controller.CustomerLogController;
import com.incture.CRM.Entity.CustomerLog;
import com.incture.CRM.Entity.Sale;
import com.incture.CRM.Service.CustomerLogService;
import com.incture.CRM.Service.SaleService;

class CustomerLogControllerTests {

    @InjectMocks
    private CustomerLogController customerLogController;

    @Mock
    private CustomerLogService customerLogService;

    @Mock
    private SaleService saleService;

    private CustomerLog customerLog;
    private Sale sale;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Sale entity
        sale = new Sale();
        sale.setId(1L);
        sale.setProduct("Test Product");
        sale.setStage("Lead");
        sale.setDealSize(1000.0);
        sale.setClosingDate(LocalDate.now().plusMonths(1));

        // Initialize CustomerLog entity
        customerLog = new CustomerLog();
        customerLog.setId(1L);
        customerLog.setCustomer("Customer 1");
        customerLog.setInteractionType("Email");
        customerLog.setNotes("Initial contact");
        customerLog.setInteractionDate(LocalDate.now());
    }

    //Test for adding a new CustomerLog for an existing sale
    @Test
    void testAddCustomerLog() {
        when(saleService.getSaleById(1L)).thenReturn(Optional.of(sale));
        when(customerLogService.saveCustomerLog(any(CustomerLog.class))).thenReturn(customerLog);

        ResponseEntity<?> response = customerLogController.addCustomerLog(1L, customerLog);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customerLog, response.getBody());

        verify(saleService, times(1)).getSaleById(1L);
        verify(customerLogService, times(1)).saveCustomerLog(any(CustomerLog.class));
    }

    //Test for adding a Customer log for a sale that doesn't exist
    @Test
    void testAddCustomerLog_SaleNotFound() {
        when(saleService.getSaleById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = customerLogController.addCustomerLog(1L, customerLog);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to add customer log. Please try again.", response.getBody());

        verify(saleService, times(1)).getSaleById(1L);
        verify(customerLogService, never()).saveCustomerLog(any(CustomerLog.class));
    }

    //Test for getting Customer Logs using sale id
    @Test
    void testGetCustomerLogsBySaleId() {
        when(customerLogService.getCustomerLogsBySaleId(1L)).thenReturn(Arrays.asList(customerLog));

        ResponseEntity<?> response = customerLogController.getCustomerLogsBySaleId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(customerLog), response.getBody());

        verify(customerLogService, times(1)).getCustomerLogsBySaleId(1L);
    }

    //Test for getting Customer Log by it's id that exists
    @Test
    void testGetCustomerLogById() {
        when(customerLogService.getCustomerLogById(1L)).thenReturn(Optional.of(customerLog));

        ResponseEntity<?> response = customerLogController.getCustomerLogById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerLog, response.getBody());

        verify(customerLogService, times(1)).getCustomerLogById(1L);
    }

    //Test for getting Customer log that doesn't exist
    @Test
    void testGetCustomerLogById_NotFound() {
        when(customerLogService.getCustomerLogById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = customerLogController.getCustomerLogById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(customerLogService, times(1)).getCustomerLogById(999L);
    }

    //Test for getting count of logs by each customer
    @Test
    void testGetCountLogsByCustomer() {
        Map<String, Long> logCount = new HashMap<>();
        logCount.put("Customer 1", 2L);
        logCount.put("Customer 2", 3L);
        when(customerLogService.countLogsByCustomer()).thenReturn(logCount);

        ResponseEntity<?> response = customerLogController.getCountLogsByCustomer();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(logCount, response.getBody());

        verify(customerLogService, times(1)).countLogsByCustomer();
    }
}
