package com.incture.CRM.Entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;	//Sale Id
    
    private String product;		// Product name
    private String stage;		// Stage of sale
    private double dealSize;	// Financial size of deal
    private LocalDate closingDate;	//Expected closing date
    
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerLog> customerLogs;

    public Sale() {
    	
    }
    
    public Sale(Long id, String product, String stage, double dealSize, LocalDate closingDate,
			List<CustomerLog> customerLogs) {
		super();
		this.id = id;
		this.product = product;
		this.stage = stage;
		this.dealSize = dealSize;
		this.closingDate = closingDate;
		this.customerLogs = customerLogs;
	}

	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public double getDealSize() {
		return dealSize;
	}

	public void setDealSize(double dealSize) {
		this.dealSize = dealSize;
	}

	public LocalDate getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
	}

	public List<CustomerLog> getCustomerLogs() {
		return customerLogs;
	}

	public void setCustomerLogs(List<CustomerLog> customerLogs) {
		this.customerLogs = customerLogs;
	}
    
	@Override
	public String toString() {
	    return "Sale [id=" + id + ", product=" + product + ", stage=" + stage + 
	           ", dealSize=" + dealSize + ", closingDate=" + closingDate + "]";
	}

}
