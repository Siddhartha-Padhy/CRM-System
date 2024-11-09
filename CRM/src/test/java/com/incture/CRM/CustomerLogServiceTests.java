package com.incture.CRM;

import com.incture.CRM.Entity.CustomerLog;
import com.incture.CRM.Entity.Sale;
import com.incture.CRM.Repository.CustomerLogRepository;
import com.incture.CRM.Service.CustomerLogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerLogServiceTests {

    @InjectMocks
    private CustomerLogService customerLogService;

    @Mock
    private CustomerLogRepository customerLogRepository;

    private CustomerLog customerLog;
    private Sale sale;

    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	
        // Initialize a sample Sale for the CustomerLog
        sale = new Sale();
        sale.setId(1L);
        sale.setProduct("Product 1");
        sale.setStage("Lead");
        sale.setDealSize(1000.0);
        sale.setClosingDate(LocalDate.now().plusDays(30));

        // Initialize a sample CustomerLog for testing
        customerLog = new CustomerLog();
        customerLog.setId(1L);
        customerLog.setCustomer("Customer 1");
        customerLog.setInteractionType("Email");
        customerLog.setNotes("Initial contact");
        customerLog.setInteractionDate(LocalDate.now());
        customerLog.setSale(sale);
    }

    //Test to save Customer Log
    @Test
    void testSaveCustomerLog() {

        when(customerLogRepository.save(any(CustomerLog.class))).thenReturn(customerLog);

        CustomerLog savedLog = customerLogService.saveCustomerLog(customerLog);

        assertNotNull(savedLog);
        assertEquals(customerLog.getCustomer(), savedLog.getCustomer());
        assertEquals(customerLog.getInteractionType(), savedLog.getInteractionType());
        assertEquals(customerLog.getNotes(), savedLog.getNotes());
        assertEquals(customerLog.getSale().getId(), savedLog.getSale().getId());
        verify(customerLogRepository, times(1)).save(customerLog);
    }

    //Test to get Customer Log by id that exists
    @Test
    void testGetCustomerLogById() {
        when(customerLogRepository.findById(1L)).thenReturn(Optional.of(customerLog));
        Optional<CustomerLog> foundLog = customerLogService.getCustomerLogById(1L);

        assertTrue(foundLog.isPresent());
        assertEquals(customerLog.getId(), foundLog.get().getId());
        assertEquals(customerLog.getCustomer(), foundLog.get().getCustomer());
        assertEquals(customerLog.getInteractionType(), foundLog.get().getInteractionType());
        assertEquals(customerLog.getNotes(), foundLog.get().getNotes());
        verify(customerLogRepository, times(1)).findById(1L);
    }

    //Test to get Customer Log that doesn't exist
    @Test
    void testGetCustomerLogByIdNotFound() {
        // Given
        when(customerLogRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<CustomerLog> foundLog = customerLogService.getCustomerLogById(1L);

        // Then
        assertFalse(foundLog.isPresent());
        verify(customerLogRepository, times(1)).findById(1L);
    }

    //Test to get count of logs by each customer when they exist
    @Test
    void testCountLogsByCustomer() {
        // Given
        CustomerLog log1 = new CustomerLog();
        log1.setCustomer("Customer 1");

        CustomerLog log2 = new CustomerLog();
        log2.setCustomer("Customer 1");

        CustomerLog log3 = new CustomerLog();
        log3.setCustomer("Customer 2");

        List<CustomerLog> logs = Arrays.asList(log1, log2, log3);
        when(customerLogRepository.findAll()).thenReturn(logs);

        // When
        Map<String, Long> logCountMap = customerLogService.countLogsByCustomer();

        // Then
        assertEquals(2L, logCountMap.get("Customer 1"));
        assertEquals(1L, logCountMap.get("Customer 2"));
        assertEquals(2, logCountMap.size());
        verify(customerLogRepository, times(1)).findAll();
    }

    //Test to get count of logs by customers when it's empty
    @Test
    void testCountLogsByCustomerEmpty() {
        // Given
        when(customerLogRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        Map<String, Long> logCountMap = customerLogService.countLogsByCustomer();

        // Then
        assertTrue(logCountMap.isEmpty());
        verify(customerLogRepository, times(1)).findAll();
    }

    //Test to get Customer Logs by sale id when sale exists
    @Test
    void testGetCustomerLogsBySaleId() {
        // Given
        CustomerLog log1 = new CustomerLog();
        log1.setSale(sale);
        log1.setCustomer("Customer 1");

        CustomerLog log2 = new CustomerLog();
        log2.setSale(sale);
        log2.setCustomer("Customer 2");

        List<CustomerLog> logs = Arrays.asList(log1, log2);
        when(customerLogRepository.findBySaleId(1L)).thenReturn(logs);

        // When
        List<CustomerLog> logsBySaleId = customerLogService.getCustomerLogsBySaleId(1L);

        // Then
        assertEquals(2, logsBySaleId.size());
        assertEquals(1L, logsBySaleId.get(0).getSale().getId());
        assertEquals(1L, logsBySaleId.get(1).getSale().getId());
        verify(customerLogRepository, times(1)).findBySaleId(1L);
    }

    //Test to get Customer Logs by sale id when sale doesn't exist
    @Test
    void testGetCustomerLogsBySaleIdNotFound() {
        // Given
        when(customerLogRepository.findBySaleId(1L)).thenReturn(Collections.emptyList());

        // When
        List<CustomerLog> logsBySaleId = customerLogService.getCustomerLogsBySaleId(1L);

        // Then
        assertTrue(logsBySaleId.isEmpty());
        verify(customerLogRepository, times(1)).findBySaleId(1L);
    }
}
