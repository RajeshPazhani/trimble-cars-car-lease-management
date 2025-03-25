package com.trimblecars.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trimblecars.dto.LeaseResponseDTO;
import com.trimblecars.entities.Car;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Lease;
import com.trimblecars.repositories.CustomerRepository;
import com.trimblecars.services.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/lease/{registrationNo}/{customerId}")
    public ResponseEntity<Lease> leaseCar(@PathVariable("registrationNo") String registrationNo, 
                                          @PathVariable("customerId") Long customerId) {
        System.out.println("Received Registration No: " + registrationNo);
        System.out.println("Received Customer ID: " + customerId);

        // Fetch the customer from the database
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Lease lease = customerService.leaseCar(registrationNo, customer);
        return ResponseEntity.ok(lease);
    }


    @PostMapping("/end-lease/{leaseId}/{customerId}")
    public ResponseEntity<LeaseResponseDTO> endLease(@PathVariable("leaseId") Long leaseId, 
                                                     @PathVariable("customerId") Long customerId) {
        System.out.println("Received Lease ID: " + leaseId);
        System.out.println("Received Customer ID: " + customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Lease lease = customerService.endLease(leaseId, customer);
        
        return ResponseEntity.ok(new LeaseResponseDTO(lease));
    }



    @GetMapping("/available-cars")
    public ResponseEntity<List<Car>> getAvailableCars() {
        return ResponseEntity.ok(customerService.getAvailableCars());
    }

    @GetMapping("/lease-history/{customerId}")
    public ResponseEntity<List<Lease>> getLeaseHistory(@PathVariable("customerId") Long customerId) {
        System.out.println("Fetching lease history for Customer ID: " + customerId);

        // Fetch the customer from the database
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Lease> leases = customerService.getCustomerLeaseHistory(customer);

        if (leases.isEmpty()) {
            System.out.println("No leases found for Customer ID: " + customerId);
        } else {
            System.out.println("Leases found: " + leases.size());
        }

        return ResponseEntity.ok(leases);
    }


}

