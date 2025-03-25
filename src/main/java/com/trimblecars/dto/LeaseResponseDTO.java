package com.trimblecars.dto;

import java.time.LocalDate;

import com.trimblecars.entities.Lease;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)  // Ignore null fields in JSON
public class LeaseResponseDTO {
    private Long leaseId;
    private Long customerId;
    private String registrationNo;
    private String pickupDate;
    private String returnDate;
    
    // ✅ Default constructor is required
    public LeaseResponseDTO() {}

    // ✅ Constructor to populate DTO
    public LeaseResponseDTO(Lease lease) {
        this.leaseId = lease.getId();
        this.customerId = lease.getCustomer().getCustomerId();
        this.registrationNo = lease.getCar().getRegistrationNo();
        this.pickupDate = lease.getPickupDate().toString();
        this.returnDate = (lease.getReturnDate() != null) ? lease.getReturnDate().toString() : null;
    }

    // ✅ Getters required for JSON serialization
    public Long getLeaseId() { return leaseId; }
    public Long getCustomerId() { return customerId; }
    public String getRegistrationNo() { return registrationNo; }
    public String getPickupDate() { return pickupDate; }
    public String getReturnDate() { return returnDate; }
}


