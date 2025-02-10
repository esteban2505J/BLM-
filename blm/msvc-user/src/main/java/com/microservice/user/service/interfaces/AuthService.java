package com.microservice.user.service.interfaces;

import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;


import org.springframework.stereotype.Service;




@Service
public interface AuthService {

    public TokenDTO register( UserDTO user);
    public TokenDTO login( LoginDTO loginDTO);
    public boolean checkToken(  TokenDTO token);
    public StateRequest forgotPassword(  String email);
    public StateRequest checkTokenPassword( TokenDTO token, String password);
    public StateRequest resetPassword(String password, String email);



}
