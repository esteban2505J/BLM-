package com.microservice.user.service.implementation;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {






    @Override
    public ResponseDTO updateUser(UserDTO userDTO) {
        return null;
    }
}
