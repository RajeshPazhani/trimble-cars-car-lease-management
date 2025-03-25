package com.trimblecars.services;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Lease;
import com.trimblecars.entities.Owner;
import com.trimblecars.repositories.LeaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaseReportServiceTest {

    @Mock
    private LeaseRepository leaseRepository;

    @InjectMocks
    private LeaseReportService leaseReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateLeaseHistoryReport() {
        // Mock lease data
        Owner owner = new Owner();
        owner.setOwnerName("A");
        owner.setContactNo("9998887776");

        Car car = new Car();
        car.setOwner(owner);

        Customer customer = new Customer();
        customer.setUserName("John Doe");
        customer.setContactNo("9876543210");

        Lease lease = new Lease();
        lease.setId(1L);
        lease.setPickupVenue("Venue A");
        lease.setReturnVenue("Venue B");
        lease.setPickupDate(LocalDate.of(2025, 3, 25));
        lease.setReturnDate(null);
        lease.setCustomer(customer);
        lease.setCar(car);

        when(leaseRepository.findAll()).thenReturn(List.of(lease));

        byte[] pdfBytes = leaseReportService.generateLeaseHistoryReport();

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        verify(leaseRepository, times(1)).findAll();
    }
}
