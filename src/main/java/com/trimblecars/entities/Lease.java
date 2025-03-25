package com.trimblecars.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Lease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String pickupVenue;
    private String returnVenue;

    @Enumerated(EnumType.STRING)
    private LeaseStatus leaseStatus; // Active, Completed, Cancelled

    private Double leaseAmount; // Total lease amount

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getPickupVenue() {
        return pickupVenue;
    }

    public void setPickupVenue(String pickupVenue) {
        this.pickupVenue = pickupVenue;
    }

    public String getReturnVenue() {
        return returnVenue;
    }

    public void setReturnVenue(String returnVenue) {
        this.returnVenue = returnVenue;
    }

    public LeaseStatus getLeaseStatus() {
        return leaseStatus;
    }

    public void setLeaseStatus(LeaseStatus leaseStatus) {
        this.leaseStatus = leaseStatus;
    }

    public Double getLeaseAmount() {
        return leaseAmount;
    }

    public void setLeaseAmount(Double leaseAmount) {
        this.leaseAmount = leaseAmount;
    }
    
    public enum LeaseStatus {
        ACTIVE, COMPLETED, CANCELLED
    }
}
