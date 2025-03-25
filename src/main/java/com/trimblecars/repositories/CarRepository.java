package com.trimblecars.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Owner;
import com.trimblecars.enums.CarStatus;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findByStatus(CarStatus status);
    List<Car> findByOwner(Owner owner);
    Optional<Car> findByRegistrationNo(String registrationNo);
}

