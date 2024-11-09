package com.incture.CRM;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.incture.CRM.Entity.Sale;
import com.incture.CRM.Repository.SaleRepository;
import com.incture.CRM.Service.SaleService;

class SaleServiceTests {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SaleService saleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Test to save a sale
    @Test
    void testSaveSale() {
        Sale sale = new Sale(1L, "Product A", "Lead", 200.0, LocalDate.now(), new ArrayList<>());
        when(saleRepository.save(sale)).thenReturn(sale);

        Sale savedSale = saleService.saveSale(sale);

        assertNotNull(savedSale);
        assertEquals(1L, savedSale.getId());
        assertEquals("Product A", savedSale.getProduct());
        assertEquals("Lead", savedSale.getStage());
        verify(saleRepository, times(1)).save(sale);
    }

    //Test to get sale by it when it exists
    @Test
    void testGetSaleById() {
        Sale sale = new Sale(1L, "Product B", "Closed", 300.0, LocalDate.now(), new ArrayList<>());
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        Optional<Sale> result = saleService.getSaleById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Product B", result.get().getProduct());
        verify(saleRepository, times(1)).findById(1L);
    }

    //Test to get sale by id when it doesn't exist
    @Test
    void testGetSaleById_NotFound() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Sale> result = saleService.getSaleById(1L);

        assertFalse(result.isPresent());
        verify(saleRepository, times(1)).findById(1L);
    }

    //Test to get all sales
    @Test
    void testGetAllSales() {
        Sale sale1 = new Sale(1L, "Product A", "Lead", 200.0, LocalDate.now(), new ArrayList<>());
        Sale sale2 = new Sale(2L, "Product B", "Closed", 300.0, LocalDate.now(), new ArrayList<>());
        when(saleRepository.findAll()).thenReturn(Arrays.asList(sale1, sale2));

        List<Sale> result = saleService.getAllSales();

        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getProduct());
        assertEquals("Product B", result.get(1).getProduct());
        verify(saleRepository, times(1)).findAll();
    }

    //Test to check if sale exists by sale id when it actually exists
    @Test
    void testSaleExistsById() {
        when(saleRepository.existsById(1L)).thenReturn(true);

        boolean exists = saleService.saleExistsById(1L);

        assertTrue(exists);
        verify(saleRepository, times(1)).existsById(1L);
    }

    //Test to check if sale exists by sale id when it actually doesn't exist
    @Test
    void testSaleExistsById_NotFound() {
        when(saleRepository.existsById(1L)).thenReturn(false);

        boolean exists = saleService.saleExistsById(1L);

        assertFalse(exists);
        verify(saleRepository, times(1)).existsById(1L);
    }

    //Test to count no. of sales in each stage
    @Test
    void testCountByStage() {
        String stage = "Closed";
        when(saleRepository.countByStage(stage)).thenReturn(5L);

        Long count = saleService.countByStage(stage);

        assertEquals(5L, count);
        verify(saleRepository, times(1)).countByStage(stage);
    }

    //Test to delete a sale by id when it exists
    @Test
    void testDeleteSaleById() {
        saleService.deleteSaleById(1L);

        verify(saleRepository, times(1)).deleteById(1L);
    }

    //Test to delete a sale by id when it doesn't exist
    @Test
    void testDeleteSaleById_NotFound() {
        doThrow(new IllegalArgumentException("Sale not found")).when(saleRepository).deleteById(99L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> saleService.deleteSaleById(99L));
        assertEquals("Sale not found", exception.getMessage());
        verify(saleRepository, times(1)).deleteById(99L);
    }

    //Test to get total deal size for closed sales
    @Test
    void testGetTotalDealSizeForClosedSales() {
        Sale closedSale1 = new Sale(1L, "Product C", "Closed", 150.0, LocalDate.now(), new ArrayList<>());
        Sale closedSale2 = new Sale(2L, "Product D", "Closed", 200.0, LocalDate.now(), new ArrayList<>());
        Sale openSale = new Sale(3L, "Product E", "Open", 50.0, LocalDate.now(), new ArrayList<>());

        when(saleRepository.findAll()).thenReturn(Arrays.asList(closedSale1, closedSale2, openSale));

        double totalDealSize = saleService.getTotalDealSizeForClosedSales();

        assertEquals(350.0, totalDealSize, 0.001);
        verify(saleRepository, times(1)).findAll();
    }

    //Test to get total deal size for unclosed sales
    @Test
    void testGetTotalDealSizeForUnclosedSales() {
        Sale closedSale = new Sale(1L, "Product C", "Closed", 100.0, LocalDate.now(), new ArrayList<>());
        Sale openSale1 = new Sale(2L, "Product F", "Open", 75.0, LocalDate.now(), new ArrayList<>());
        Sale openSale2 = new Sale(3L, "Product G", "In Progress", 125.0, LocalDate.now(), new ArrayList<>());

        when(saleRepository.findAll()).thenReturn(Arrays.asList(closedSale, openSale1, openSale2));

        double totalDealSize = saleService.getTotalDealSizeForUnclosedSales();

        assertEquals(200.0, totalDealSize, 0.001);
        verify(saleRepository, times(1)).findAll();
    }
}
