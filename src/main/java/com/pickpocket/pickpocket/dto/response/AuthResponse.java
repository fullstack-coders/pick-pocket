package com.pickpocket.pickpocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data //replaces @getter and @setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private Long id;
    private String name;
    private String email;
    private String message;
    private LocalDateTime createdAt;
}