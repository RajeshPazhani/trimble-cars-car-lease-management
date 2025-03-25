package com.trimblecars.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Lease;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {
    List<Lease> findByCustomer(Customer customer);
    List<Lease> findByCar(Car car);
}

