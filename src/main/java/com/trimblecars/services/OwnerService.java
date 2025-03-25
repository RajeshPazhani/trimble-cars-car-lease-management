package com.trimblecars.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Lease;
import com.trimblecars.entities.Owner;
import com.trimblecars.enums.CarStatus;
import com.trimblecars.repositories.CarRepository;
import com.trimblecars.repositories.LeaseRepository;
import com.trimblecars.repositories.OwnerRepository;

@Service
public class OwnerService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LeaseRepository leaseRepository;
    
    @Autowired
    private OwnerRepository ownerRepository;

    public Owner findOwnerById(Long ownerId) {
        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> {
                    System.out.println("Owner not found with ID: " + ownerId);
                    return new RuntimeException("Owner not found with id: " + ownerId);
                });
    }

    public Car enrollCar(Car car, Owner owner) {
        car.setOwner(owner); // Associate the car with the owner
        owner.getCars().add(car); // Add car to owner's list
        ownerRepository.save(owner); // Save the owner with updated car list
        return carRepository.save(car); // Save and return the car
    }
    
    public List<Car> getOwnerCars(Owner owner) {
        return carRepository.findByOwner(owner); // Fetch cars owned by the owner
    }




    public List<Lease> getLeasedCarHistory(Owner owner) {
        List<Car> ownedCars = carRepository.findByOwner(owner);
        return ownedCars.stream()
                .flatMap(car -> leaseRepository.findByCar(car).stream())
                .collect(Collectors.toList());
    }
}
