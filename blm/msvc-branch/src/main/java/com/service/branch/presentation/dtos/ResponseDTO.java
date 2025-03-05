package com.service.branch.presentation.dtos;


import com.service.branch.persistence.model.enums.StateRequest;

public record ResponseDTO<T>(

        StateRequest stateRequest,
        T message
) {
}
