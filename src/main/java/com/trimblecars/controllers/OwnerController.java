package com.trimblecars.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Lease;
import com.trimblecars.entities.Owner;
import com.trimblecars.repositories.OwnerRepository;
import com.trimblecars.services.OwnerService;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;
    
    @GetMapping("/owner/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ownerService.findOwnerById(id));
    }

    @PostMapping("/enroll/{ownerId}")
    public ResponseEntity<Car> enrollCar(@PathVariable("ownerId") Long ownerId, @RequestBody Car car)
 {
        Owner owner = ownerService.findOwnerById(ownerId);
        car.setOwner(owner);
        return ResponseEntity.ok(ownerService.enrollCar(car, owner));
    }
    @GetMapping("/cars/{ownerId}")
    public ResponseEntity<List<Car>> getOwnerCars(@PathVariable("ownerId") Long ownerId) {
        Owner owner = ownerService.findOwnerById(ownerId);
        List<Car> cars = ownerService.getOwnerCars(owner);
        return ResponseEntity.ok(cars);
    }


    @GetMapping("/leased-history/{ownerId}")
    public ResponseEntity<List<Lease>> getLeasedCarHistory(@PathVariable("ownerId") Long ownerId) {
        System.out.println("Fetching lease history for Owner ID: " + ownerId);

        // Fetch the owner from the database
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        List<Lease> leases = ownerService.getLeasedCarHistory(owner);

        if (leases.isEmpty()) {
            System.out.println("No leases found for Owner ID: " + ownerId);
        } else {
            System.out.println("Leases found: " + leases.size());
        }

        return ResponseEntity.ok(leases);
    }

}

