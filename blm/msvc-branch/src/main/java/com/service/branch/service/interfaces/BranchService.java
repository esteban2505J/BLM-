package com.service.branch.service.interfaces;


import com.service.branch.presentation.dtos.EmployeeDTO;
import com.service.branch.presentation.dtos.ResponseDTO;

public interface BranchService {

    public ResponseDTO registerEmployee(EmployeeDTO employeeDTO);
}
