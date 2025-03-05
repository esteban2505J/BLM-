package com.microservice.user.presentation.dtos;

import com.microservice.user.persistence.model.entities.RoleEntity;
import com.microservice.user.persistence.model.enums.Status;

import java.util.List;
import java.util.Set;


public record EmployeeDTO(
        String firstName,
        String lastName,
        String userName,
        String email,
        String phoneNumber,
        String employeeCode,
        Set<String> branchIds,
        Status status,
        Set<RoleEntity> roles
) {}
