package com.service.branch.service.interfaces;

import com.service.branch.persistence.model.enums.StateRequest;
import com.service.branch.presentation.dtos.EmployeeDTO;

public interface BranchService {

    public StateRequest registerEmployee(EmployeeDTO employeeDTO);
}
