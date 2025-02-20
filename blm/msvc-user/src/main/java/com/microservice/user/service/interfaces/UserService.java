package com.microservice.user.service.interfaces;


import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;

import java.util.List;


public interface UserService {


    public ResponseDTO<String> updateUser(UserDTO userDTO);
    public StateRequest deleteUser(String  email);
    public UserDTO addRoleToUser(String  email);

    public StateRequest addRoleToUser(String email, String nameRole);

    public List<UserDTO> getUserByRol(String  email);
    public List<UserDTO> getAllUsers();
    public StateRequest activateUser(String  email);




    
}