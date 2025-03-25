package com.trimblecars.controllers;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trimblecars.dto.AuthResponseDTO;
import com.trimblecars.entities.Admin;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Owner;
import com.trimblecars.repositories.AdminRepository;
import com.trimblecars.repositories.CustomerRepository;
import com.trimblecars.repositories.OwnerRepository;
import com.trimblecars.services.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private OwnerRepository ownerRepository;
    
    @Mock
    private AdminRepository adminRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AuthService authService;
    
    private Customer customer;
    private Owner owner;
    private Admin admin;
    
    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setUserName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");
        
        owner = new Owner();
        owner.setOwnerName("A");
        owner.setEmail("a@example.com");
        owner.setPassword("adminPass123");
        
        admin = new Admin();
        admin.setAdminName("AdminUser");
        admin.setEmail("admin@example.com");
        admin.setPassword("adminPassword");
    }
    
    @Test
    void testRegisterCustomer_Success() {
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(customer.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        AuthResponseDTO response = authService.registerCustomer(customer);
        
        assertNotNull(response);
        assertEquals("Customer registered successfully!", response.getMessage());
    }
    
    @Test
    void testRegisterCustomer_Failure_AlreadyExists() {
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        
        AuthResponseDTO response = authService.registerCustomer(customer);
        
        assertNotNull(response);
        assertEquals("Customer already registered!", response.getMessage());
    }
    
    @Test
    void testRegisterOwner_Success() {
        when(ownerRepository.findByEmail(owner.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(owner.getPassword())).thenReturn("encodedPassword");
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);
        
        AuthResponseDTO response = authService.registerOwner(owner);
        
        assertNotNull(response);
        assertEquals("Owner registered successfully!", response.getMessage());
    }
    
    @Test
    void testRegisterAdmin_Success() {
        when(adminRepository.findByEmail(admin.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(admin.getPassword())).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);
        
        AuthResponseDTO response = authService.registerAdmin(admin);
        
        assertNotNull(response);
        assertEquals("Admin registered successfully!", response.getMessage());
    }
}
