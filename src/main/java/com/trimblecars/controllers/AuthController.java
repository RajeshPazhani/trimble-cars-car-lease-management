package com.trimblecars.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trimblecars.dto.AuthResponseDTO;
import com.trimblecars.entities.Admin;
import com.trimblecars.entities.Customer;
import com.trimblecars.entities.Owner;
import com.trimblecars.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponseDTO> registerCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(authService.registerCustomer(customer));
    }

    @PostMapping("/register/owner")
    public ResponseEntity<AuthResponseDTO> registerOwner(@RequestBody Owner owner) {
        return ResponseEntity.ok(authService.registerOwner(owner));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponseDTO> registerAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(authService.registerAdmin(admin));
    }
}
