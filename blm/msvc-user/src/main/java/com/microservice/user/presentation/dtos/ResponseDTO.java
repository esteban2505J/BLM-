package com.microservice.user.presentation.dtos;

import com.microservice.user.persitence.model.enums.StateRequest;

public record ResponseDTO<T>(

        StateRequest stateRequest,
        T message
) {
}
