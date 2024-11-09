package com.incture.CRM.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CustomerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;	//CustomerLog id
    
    private String customer;	//Customer name for the log
    private String interactionType;		//Interaction type
    private String notes;		// Interaction notes
    private LocalDate interactionDate;	//Interaction date
    
    @ManyToOne
    @JsonIgnore
    private Sale sale;

    // Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(String interactionType) {
		this.interactionType = interactionType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDate getInteractionDate() {
		return interactionDate;
	}

	public void setInteractionDate(LocalDate interactionDate) {
		this.interactionDate = interactionDate;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	@Override
	public String toString() {
		return "CustomerLog [id=" + id + ", customer=" + customer + ", interactionType=" + interactionType + ", notes="
				+ notes + ", interactionDate=" + interactionDate + ", sale=" + sale + "]";
	}
    
	
}
