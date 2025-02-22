package com.microservice.user.service.interfaces;
import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.persitence.model.enums.Status;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;


import java.util.List;


public interface UserService {


    public ResponseDTO<String> updateUser(UserDTO userDTO);
    public StateRequest deleteUser(String  email);
    public StateRequest addRoleToUser(String email, String nameRole);
    public StateRequest removeRoleFromUser(String email, String nameRole);
    public List<UserEntity> getUserByRol(String nameRole);
    public List<UserEntity> getAllUsers(Status status);
    public StateRequest activateUser(String  email);




    
}