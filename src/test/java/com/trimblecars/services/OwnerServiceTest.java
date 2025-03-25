package com.trimblecars.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Lease;
import com.trimblecars.entities.Owner;
import com.trimblecars.repositories.CarRepository;
import com.trimblecars.repositories.LeaseRepository;
import com.trimblecars.repositories.OwnerRepository;

class OwnerServiceTest {
    
    @InjectMocks
    private OwnerService ownerService;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private LeaseRepository leaseRepository;

    private Owner owner;
    private Car car;
    private Lease lease;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        owner = new Owner();
        owner.setOwnerId(1L);
        owner.setOwnerName("Test Owner");
        owner.setCars(new ArrayList<>());  // Initialize the cars list

        
        car = new Car();
        car.setRegistrationNo("MH12AB1234");
        car.setOwner(owner);
        
        lease = new Lease();
        lease.setCar(car);
    }

    @Test
    void testFindOwnerById_Found() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        Owner foundOwner = ownerService.findOwnerById(1L);
        assertNotNull(foundOwner);
        assertEquals("Test Owner", foundOwner.getOwnerName());
        verify(ownerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindOwnerById_NotFound() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> ownerService.findOwnerById(1L));
        assertEquals("Owner not found with id: 1", exception.getMessage());
    }

    @Test
    void testEnrollCar() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(carRepository.save(any(Car.class))).thenReturn(car);
        
        Car enrolledCar = ownerService.enrollCar(car, owner);
        assertNotNull(enrolledCar);
        assertEquals("MH12AB1234", enrolledCar.getRegistrationNo());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testGetOwnerCars() {
        when(carRepository.findByOwner(owner)).thenReturn(Arrays.asList(car));
        List<Car> cars = ownerService.getOwnerCars(owner);
        assertNotNull(cars);
        assertEquals(1, cars.size());
        verify(carRepository, times(1)).findByOwner(owner);
    }

    @Test
    void testGetLeasedCarHistory() {
        when(carRepository.findByOwner(owner)).thenReturn(Arrays.asList(car));
        when(leaseRepository.findByCar(car)).thenReturn(Arrays.asList(lease));
        
        List<Lease> leases = ownerService.getLeasedCarHistory(owner);
        assertNotNull(leases);
        assertEquals(1, leases.size());
        verify(leaseRepository, times(1)).findByCar(car);
    }
}
