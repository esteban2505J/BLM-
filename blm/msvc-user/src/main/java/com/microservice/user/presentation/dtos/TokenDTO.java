package com.microservice.user.presentation.dtos;

import jakarta.validation.constraints.NotBlank;

public record TokenDTO(
        @NotBlank(message = "token is required")
        String token

) {


}
