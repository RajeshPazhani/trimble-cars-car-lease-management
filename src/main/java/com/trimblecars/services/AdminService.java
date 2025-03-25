package com.trimblecars.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trimblecars.entities.*;
import com.trimblecars.enums.CarStatus;
import com.trimblecars.repositories.CarRepository;
import com.trimblecars.repositories.LeaseRepository;
import com.trimblecars.repositories.OwnerRepository;
import com.trimblecars.repositories.CustomerRepository;


@Service
public class AdminService {
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private LeaseRepository leaseRepository;
    
    @Autowired
    private OwnerRepository ownerRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public List<Lease> getAllLeases() {
        return leaseRepository.findAll();
    }

    // ✅ Lease a car (As Customer)
    public Lease leaseCar(String registrationNo, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Lease> activeLeases = leaseRepository.findByCustomer(customer);
        if (activeLeases.size() >= 2) {
            throw new RuntimeException("Customer can lease a maximum of 2 cars at a time.");
        }

        Car car = carRepository.findById(registrationNo)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (car.getStatus() != CarStatus.IDLE) {
            throw new RuntimeException("Car is not available for lease.");
        }

        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setPickupDate(java.time.LocalDate.now());

        car.setStatus(CarStatus.ON_LEASE);
        carRepository.save(car);
        leaseRepository.save(lease);

        return lease;  // Returning full Lease details
    }

    // ✅ Enroll a car (As Owner)
    public Car enrollCar(Car car, Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        car.setOwner(owner);
        owner.getCars().add(car);
        ownerRepository.save(owner);
        return carRepository.save(car);
    }

    //  Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    //  Get all owners
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    //  Get available cars
    public List<Car> getAvailableCars() {
        return carRepository.findByStatus(CarStatus.IDLE);
    }
    public Lease endLease(Long leaseId) {
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new RuntimeException("Lease record not found"));

        lease.setReturnDate(LocalDate.now());
        lease.getCar().setStatus(CarStatus.IDLE);

        carRepository.save(lease.getCar());
        leaseRepository.save(lease);

        return lease;  // Returning the updated Lease
    }

}
