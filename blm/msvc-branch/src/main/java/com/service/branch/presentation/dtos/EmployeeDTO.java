package com.service.branch.presentation.dtos;


import com.service.branch.persistence.model.enums.Status;

import java.util.Set;

public record EmployeeDTO(
        String firstName,
        String lastName,
        String userName,
        String email,
        String phoneNumber,
        String employeeCode,
        Set<String> branchIds,
        Status status
) {}
