package com.trimblecars.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trimblecars.dto.AuthResponseDTO;
import com.trimblecars.entities.Admin;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Owner;
import com.trimblecars.repositories.AdminRepository;
import com.trimblecars.repositories.CustomerRepository;
import com.trimblecars.repositories.OwnerRepository;

@Service
public class AuthService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponseDTO registerCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            return new AuthResponseDTO(null, null, null, "Customer already registered!");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        return new AuthResponseDTO(savedCustomer.getCustomerId(), savedCustomer.getUserName(), savedCustomer.getEmail(), "Customer registered successfully!");
    }

    public AuthResponseDTO registerOwner(Owner owner) {
        if (ownerRepository.findByEmail(owner.getEmail()).isPresent()) {
            return new AuthResponseDTO(null, null, null, "Owner already registered!");
        }
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        Owner savedOwner = ownerRepository.save(owner);
        return new AuthResponseDTO(savedOwner.getOwnerId(), savedOwner.getOwnerName(), savedOwner.getEmail(), "Owner registered successfully!");
    }

    public AuthResponseDTO registerAdmin(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            return new AuthResponseDTO(null, null, null, "Admin already registered!");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminRepository.save(admin);
        return new AuthResponseDTO(savedAdmin.getAdminId(), savedAdmin.getAdminName(), savedAdmin.getEmail(), "Admin registered successfully!");
    }
}
