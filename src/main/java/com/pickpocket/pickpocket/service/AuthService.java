package com.pickpocket.pickpocket.service;

import com.pickpocket.pickpocket.dto.request.RegisterRequest;
import com.pickpocket.pickpocket.dto.response.AuthResponse;
import com.pickpocket.pickpocket.entity.User;
import com.pickpocket.pickpocket.exception.UserAlreadyExistsException;
import com.pickpocket.pickpocket.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByName(request.getName())) {
            throw new UserAlreadyExistsException("Username '" + request.getName() + "' is already taken.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email '" + request.getEmail() + "' is already in use.");
        }

        // Using Lombok @Builder on User (if added to User entity)
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        // Using Lombok @Builder on UserResponse
        return AuthResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }
}