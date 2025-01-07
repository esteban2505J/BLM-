package com.microservice.user.presentation.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        String password
) {
}
