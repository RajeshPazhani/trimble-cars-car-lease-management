package com.trimblecars.services;

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

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Lease;
import com.trimblecars.enums.CarStatus;
import com.trimblecars.repositories.CarRepository;
import com.trimblecars.repositories.LeaseRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private LeaseRepository leaseRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CustomerService customerService;

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
        lease.setPickupDate(LocalDate.now());
    }

    @Test
    void testLeaseCar_Success() {
        when(leaseRepository.findByCustomer(customer)).thenReturn(Arrays.asList());
        when(carRepository.findByRegistrationNo("MH12AB1234")).thenReturn(Optional.of(car));
        when(leaseRepository.save(any(Lease.class))).thenReturn(lease);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Lease leasedCar = customerService.leaseCar("MH12AB1234", customer);
        
        assertNotNull(leasedCar);
        assertEquals("MH12AB1234", leasedCar.getCar().getRegistrationNo());
        assertEquals(customer, leasedCar.getCustomer());
    }

    @Test
    void testLeaseCar_ExceedLimit() {
        when(leaseRepository.findByCustomer(customer)).thenReturn(Arrays.asList(new Lease(), new Lease()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.leaseCar("MH12AB1234", customer);
        });

        assertEquals("You can lease a maximum of 2 cars at a time.", exception.getMessage());
    }

    @Test
    void testLeaseCar_CarNotAvailable() {
        car.setStatus(CarStatus.ON_LEASE);
        when(leaseRepository.findByCustomer(customer)).thenReturn(Arrays.asList());
        when(carRepository.findByRegistrationNo("MH12AB1234")).thenReturn(Optional.of(car));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.leaseCar("MH12AB1234", customer);
        });

        assertEquals("Car is not available for lease.", exception.getMessage());
    }

    @Test
    void testEndLease_Success() {
        when(leaseRepository.findById(1L)).thenReturn(Optional.of(lease));
        when(leaseRepository.save(any(Lease.class))).thenReturn(lease);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Lease endedLease = customerService.endLease(1L, customer);

        assertNotNull(endedLease);
        assertEquals(LocalDate.now(), endedLease.getReturnDate());
        assertEquals(CarStatus.IDLE, endedLease.getCar().getStatus());
    }

    @Test
    void testEndLease_Unauthorized() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setCustomerId(2L);

        when(leaseRepository.findById(1L)).thenReturn(Optional.of(lease));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.endLease(1L, anotherCustomer);
        });

        assertEquals("Unauthorized action!", exception.getMessage());
    }

    @Test
    void testGetAvailableCars() {
        when(carRepository.findByStatus(CarStatus.IDLE)).thenReturn(Arrays.asList(car));
        List<Car> availableCars = customerService.getAvailableCars();
        
        assertNotNull(availableCars);
        assertFalse(availableCars.isEmpty());
        assertEquals("MH12AB1234", availableCars.get(0).getRegistrationNo());
    }

    @Test
    void testGetCustomerLeaseHistory() {
        when(leaseRepository.findByCustomer(customer)).thenReturn(Arrays.asList(lease));
        List<Lease> leases = customerService.getCustomerLeaseHistory(customer);
        
        assertNotNull(leases);
        assertFalse(leases.isEmpty());
        assertEquals("MH12AB1234", leases.get(0).getCar().getRegistrationNo());
    }
}