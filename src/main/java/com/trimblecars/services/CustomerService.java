package com.trimblecars.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Lease;
import com.trimblecars.enums.CarStatus;
import com.trimblecars.repositories.CarRepository;
import com.trimblecars.repositories.LeaseRepository;

@Service
public class CustomerService {
    @Autowired
    private LeaseRepository leaseRepository;

    @Autowired
    private CarRepository carRepository;

    public Lease leaseCar(String registrationNo, Customer customer) {
        List<Lease> activeLeases = leaseRepository.findByCustomer(customer);
        if (activeLeases.size() >= 2) {
            throw new RuntimeException("You can lease a maximum of 2 cars at a time.");
        }

        Car car = carRepository.findByRegistrationNo(registrationNo)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (car.getStatus() != CarStatus.IDLE) {
            throw new RuntimeException("Car is not available for lease.");
        }

        Lease lease = new Lease();
        lease.setCar(car);
        lease.setCustomer(customer);
        lease.setPickupDate(LocalDate.now());

        car.setStatus(CarStatus.ON_LEASE);
        carRepository.save(car);
        leaseRepository.save(lease);

        return lease; // Return lease details instead of just a message
    }



    public Lease endLease(Long leaseId, Customer customer) {
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new RuntimeException("Lease record not found"));

        if (!lease.getCustomer().equals(customer)) {
            throw new RuntimeException("Unauthorized action!");
        }

        lease.setReturnDate(LocalDate.now());
        lease.getCar().setStatus(CarStatus.IDLE);
        
        carRepository.save(lease.getCar());
        leaseRepository.save(lease);

        return lease;  // Returning updated lease details
    }


    public List<Car> getAvailableCars() {
        return carRepository.findByStatus(CarStatus.IDLE);
    }

    public List<Lease> getCustomerLeaseHistory(Customer customer) {
        System.out.println("Finding leases for Customer ID: " + customer.getCustomerId());

        List<Lease> leases = leaseRepository.findByCustomer(customer);

        System.out.println("Lease count: " + leases.size());

        return leases;
    }

}

