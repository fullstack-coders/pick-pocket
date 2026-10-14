package com.pickpocket.pickpocket.controller;

import com.pickpocket.pickpocket.dto.request.RegisterRequest;
import com.pickpocket.pickpocket.dto.response.AuthResponse;
import com.pickpocket.pickpocket.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pickpocket/auth")
public class RegistrationController {

    private final AuthService authService;

    public RegistrationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
