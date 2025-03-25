package com.trimblecars.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trimblecars.entities.*;
import com.trimblecars.enums.CarStatus;
import com.trimblecars.repositories.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private LeaseRepository leaseRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;
    private Owner owner;
    private Car car;
    private Lease lease;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);

        owner = new Owner();
        owner.setOwnerId(1L);
        owner.setCars(new ArrayList<>());  // Fix: Initialize cars list


        car = new Car();
        car.setRegistrationNo("MH12AB1234");
        car.setStatus(CarStatus.IDLE);
        car.setOwner(owner);

        lease = new Lease();
        lease.setId(1L);
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setPickupDate(LocalDate.now());
    }

    @Test
    void testLeaseCar_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(leaseRepository.findByCustomer(customer)).thenReturn(Arrays.asList());
        when(carRepository.findById("MH12AB1234")).thenReturn(Optional.of(car));
        when(leaseRepository.save(any(Lease.class))).thenReturn(lease);

        Lease leasedCar = adminService.leaseCar("MH12AB1234", 1L);

        assertNotNull(leasedCar);
        assertEquals("MH12AB1234", leasedCar.getCar().getRegistrationNo());
        verify(leaseRepository, times(1)).save(any(Lease.class));
    }

    @Test
    void testEnrollCar_Success() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car enrolledCar = adminService.enrollCar(car, 1L);

        assertNotNull(enrolledCar);
        assertEquals("MH12AB1234", enrolledCar.getRegistrationNo());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        List<Customer> customers = adminService.getAllCustomers();
        assertEquals(1, customers.size());
    }

    @Test
    void testGetAllOwners() {
        when(ownerRepository.findAll()).thenReturn(Arrays.asList(owner));
        List<Owner> owners = adminService.getAllOwners();
        assertEquals(1, owners.size());
    }

    @Test
    void testGetAvailableCars() {
        when(carRepository.findByStatus(CarStatus.IDLE)).thenReturn(Arrays.asList(car));
        List<Car> availableCars = adminService.getAvailableCars();
        assertEquals(1, availableCars.size());
    }

    @Test
    void testEndLease_Success() {
        when(leaseRepository.findById(1L)).thenReturn(Optional.of(lease));
        when(leaseRepository.save(any(Lease.class))).thenReturn(lease);

        Lease endedLease = adminService.endLease(1L);

        assertNotNull(endedLease);
        assertEquals(LocalDate.now(), endedLease.getReturnDate());
        verify(leaseRepository, times(1)).save(any(Lease.class));
    }
}
