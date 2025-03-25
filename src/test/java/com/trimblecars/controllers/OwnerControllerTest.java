package com.trimblecars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.trimblecars.entities.Car;
import com.trimblecars.entities.Lease;
import com.trimblecars.entities.Owner;
import com.trimblecars.repositories.OwnerRepository;
import com.trimblecars.services.OwnerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

class OwnerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OwnerService ownerService;

    @Mock
    private OwnerRepository ownerRepository;
    
    @InjectMocks
    private OwnerController ownerController;

    private Owner owner;
    private Car car;
    private Lease lease;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();

        owner = new Owner();
        owner.setOwnerId(1L);
        owner.setOwnerName("A");

        car = new Car();
        car.setRegistrationNo("MH12AB1234");
        car.setOwner(owner);

        lease = new Lease();
        lease.setCar(car);
        
        when(ownerService.findOwnerById(1L)).thenReturn(owner);
        when(ownerRepository.findById(1L)).thenReturn(java.util.Optional.of(owner));
    }

    @Test
    void testGetOwnerById() throws Exception {
        when(ownerService.findOwnerById(1L)).thenReturn(owner);

        mockMvc.perform(MockMvcRequestBuilders.get("/owner/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("A"));

        verify(ownerService, times(1)).findOwnerById(1L);
    }

    @Test
    void testEnrollCar() throws Exception {
        when(ownerService.findOwnerById(1L)).thenReturn(owner);
        when(ownerService.enrollCar(any(Car.class), any(Owner.class))).thenReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.post("/owner/enroll/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNo").value("MH12AB1234"));

        verify(ownerService, times(1)).enrollCar(any(Car.class), any(Owner.class));
    }

    @Test
    void testGetOwnerCars() throws Exception {
        List<Car> cars = Arrays.asList(car);
        when(ownerService.findOwnerById(1L)).thenReturn(owner);
        when(ownerService.getOwnerCars(owner)).thenReturn(cars);

        mockMvc.perform(MockMvcRequestBuilders.get("/owner/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].registrationNo").value("MH12AB1234"));

        verify(ownerService, times(1)).getOwnerCars(owner);
    }

    @Test
    void testGetLeasedCarHistory() throws Exception {
        List<Lease> leases = Arrays.asList(lease);
        when(ownerService.findOwnerById(1L)).thenReturn(owner);
        when(ownerService.getLeasedCarHistory(owner)).thenReturn(leases);

        mockMvc.perform(MockMvcRequestBuilders.get("/owner/leased-history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(ownerService, times(1)).getLeasedCarHistory(owner);
    }
}
