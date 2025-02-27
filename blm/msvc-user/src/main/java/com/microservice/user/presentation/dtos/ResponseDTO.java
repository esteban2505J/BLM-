package com.microservice.user.presentation.dtos;

import com.microservice.user.persistence.model.enums.StateRequest;

public record ResponseDTO<T>(

        StateRequest stateRequest,
        T message
) {
}
