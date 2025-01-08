package com.microservice.user.presentation.dtos;

import com.microservice.user.persitence.model.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(

        @NotBlank(message = "role is required")
        Role role,
        @NotBlank(message = "firstName is required")
        String firstName,
        @NotBlank(message = "lastName is required")
        String lastName,
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        String password

) {

}
