package com.microservice.user.presentation.dtos;

import com.microservice.user.persitence.model.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(


        Role role,
        @NotBlank(message = "firstName is required")
        String firstName,
        @NotBlank(message = "lastName is required")
        String lastName,
        @NotBlank(message = "email is required")
        String email,
        String password,
        @NotBlank(message = "phoneNumber is required")
        String phoneNumber
) {

}
