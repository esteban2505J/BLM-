package com.microservice.user.presentation.dtos;

import jakarta.validation.constraints.NotBlank;

public record TokenDTO(
        @NotBlank(message = "IdUser is required")
        String token
) {


}
