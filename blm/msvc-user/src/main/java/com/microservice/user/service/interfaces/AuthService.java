package com.microservice.user.service.interfaces;

import com.microservice.user.persistence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.*;


import org.springframework.stereotype.Service;




@Service
public interface AuthService {

    public TokenDTO register( UserDTO user);
    public TokenDTO login( LoginDTO loginDTO);
    public boolean checkToken(  TokenDTO token);
    public StateRequest forgotPassword(  String email);
    public StateRequest checkTokenPassword( TokenDTO token, String password);
    public StateRequest resetPassword(LoginDTO loginDTO);
    public StateRequest logout(String email);
    public ResponseDTO<TokenDTO> creteAnEmployee(EmployeeDTO employee);



}
