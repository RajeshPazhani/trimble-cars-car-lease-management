package com.trimblecars.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trimblecars.dto.LeaseResponseDTO;
import com.trimblecars.entities.*;
import com.trimblecars.repositories.*;
import com.trimblecars.services.AdminService;
import com.trimblecars.services.CustomerService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private LeaseRepository leaseRepository;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private  CustomerRepository customerRepository;

    // ✅ Get all lease history (Existing Feature)
    @GetMapping("/all-lease-history")
    public ResponseEntity<List<Lease>> getAllLeaseHistory() {
        return ResponseEntity.ok(leaseRepository.findAll());
    }

    // ✅ Lease a car (As Customer)
    @PostMapping("/lease/{registrationNo}/{customerId}")
    public ResponseEntity<Lease> leaseCar(@PathVariable("registrationNo") String registrationNo, 
                                          @PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(adminService.leaseCar(registrationNo, customerId));
    }

    // ✅ Enroll a car (As Owner)
    @PostMapping("/enroll/{ownerId}")
    public ResponseEntity<Car> enrollCar(@PathVariable("ownerId") Long ownerId, @RequestBody Car car) {
        return ResponseEntity.ok(adminService.enrollCar(car, ownerId));
    }

    // ✅ Get list of all customers
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomers());
    }

    // ✅ Get list of all owners
    @GetMapping("/owners")
    public ResponseEntity<List<Owner>> getAllOwners() {
        return ResponseEntity.ok(adminService.getAllOwners());
    }

    // ✅ Get list of all available cars
    @GetMapping("/available-cars")
    public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(adminService.getAvailableCars());
    }
    
    @PostMapping("/end-lease/{leaseId}")
    public ResponseEntity<LeaseResponseDTO> endLease(@PathVariable("leaseId") Long leaseId) {
        Lease lease = adminService.endLease(leaseId);
        return ResponseEntity.ok(new LeaseResponseDTO(lease));
    }



}
