package com.trimblecars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.trimblecars.dto.LeaseResponseDTO;
import com.trimblecars.entities.Car;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Lease;
import com.trimblecars.enums.CarStatus;
import com.trimblecars.repositories.CustomerRepository;
import com.trimblecars.services.CustomerService;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;
    
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;
    private Car car;
    private Lease lease;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);

        car = new Car();
        car.setRegistrationNo("MH12AB1234");
        car.setStatus(CarStatus.IDLE);

        lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
    }

    @Test
    void testLeaseCar_Success() {
        Lease lease = new Lease();
        Car car = new Car();
        car.setRegistrationNo("MH12AB1234");
        lease.setCar(car);
        lease.setPickupDate(LocalDate.now()); // ✅ Set pickup date manually

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerService.leaseCar("MH12AB1234", customer)).thenReturn(lease);

        ResponseEntity<Lease> response = customerController.leaseCar("MH12AB1234", 1L);

        assertNotNull(response.getBody());
        assertEquals("MH12AB1234", response.getBody().getCar().getRegistrationNo());
    }



    @Test
    void testEndLease_Success() {
        lease.setPickupDate(LocalDate.now()); 

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerService.endLease(1L, customer)).thenReturn(lease);
        
        ResponseEntity<LeaseResponseDTO> response = customerController.endLease(1L, 1L);
        
        assertNotNull(response.getBody());
        assertEquals("MH12AB1234", response.getBody().getRegistrationNo());
    }

    @Test
    void testGetAvailableCars() {
        List<Car> cars = Arrays.asList(car);
        when(customerService.getAvailableCars()).thenReturn(cars);
        
        ResponseEntity<List<Car>> response = customerController.getAvailableCars();
        
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("MH12AB1234", response.getBody().get(0).getRegistrationNo());
    }

    @Test
    void testGetLeaseHistory() {
        List<Lease> leases = Arrays.asList(lease);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerService.getCustomerLeaseHistory(customer)).thenReturn(leases);
        
        ResponseEntity<List<Lease>> response = customerController.getLeaseHistory(1L);
        
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("MH12AB1234", response.getBody().get(0).getCar().getRegistrationNo());
    }
}
