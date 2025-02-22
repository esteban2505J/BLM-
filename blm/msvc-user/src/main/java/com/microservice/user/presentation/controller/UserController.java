package com.microservice.user.presentation.controller;


import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.persitence.model.enums.Status;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.implementation.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userController")
@PreAuthorize("denyAll()")
public class UserController {

    UserServiceImpl userService;


    @PutMapping("/updateUser")
    public ResponseEntity<ResponseDTO<StateRequest>> updateUserController(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO<StateRequest> responseDTO = userService.updateUser(userDTO);
        if (responseDTO.stateRequest() == StateRequest.SUCCESS) {
            return ResponseEntity.ok().body(responseDTO);
        }
        return ResponseEntity.status(400).body(responseDTO);

    }

    @PatchMapping("/deleteUser")
    public ResponseEntity <ResponseDTO<String>> deleteUserController(@RequestParam String email){
        if (userService.deleteUser(email) == StateRequest.SUCCESS) return ResponseEntity.ok().body(new ResponseDTO<String>(StateRequest.SUCCESS, "The user have been deleted"));

        return ResponseEntity.status(400).body(new ResponseDTO<String>(StateRequest.ERROR, "The user could not be deleted"));


    }

    @PatchMapping("/addRoleToUser")
    public ResponseEntity <ResponseDTO<String>> addRoleToUserController(@RequestParam String email, @RequestParam(required = true) String role){
        if (userService.addRoleToUser(email, role) == StateRequest.SUCCESS) return ResponseEntity.ok().body(new ResponseDTO<String>(StateRequest.SUCCESS, "The role has been added to the user "));

        return ResponseEntity.status(400).body(new ResponseDTO<String>(StateRequest.ERROR, "The role could not be added to the user "));
    }

    @PatchMapping("/removeRoleFromUser")
    public ResponseEntity<ResponseDTO<String>> removeRoleFromUserController(@RequestParam String email, @RequestParam String role){
        if (userService.removeRoleFromUser(email, role) == StateRequest.SUCCESS) return ResponseEntity.ok().body(new ResponseDTO<String>(StateRequest.SUCCESS, "The role has been removed from the user "));

        return ResponseEntity.status(400).body(new ResponseDTO<String>(StateRequest.ERROR, "The role could not be removed from the user "));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseDTO<List<UserEntity>>> getAllUsersController(@RequestParam Status status){

        return ResponseEntity.ok().body(new ResponseDTO<List<UserEntity>>(StateRequest.SUCCESS,userService.getAllUsers(status)));

    }

    @GetMapping("/getUsersByRol")
    public ResponseEntity<ResponseDTO<List<UserEntity>>> getUsersByRolController(@RequestParam String rol){
        return ResponseEntity.ok().body(new ResponseDTO<List<UserEntity>>(StateRequest.SUCCESS, userService.getUserByRol(rol)));
    }

    @PatchMapping("/activateUser")
    public ResponseEntity<ResponseDTO<String>> activateUserController(@RequestParam String email){
        StateRequest ActivatedUser = userService.activateUser(email);
        if (ActivatedUser == StateRequest.SUCCESS) return ResponseEntity.ok().body(new ResponseDTO<>(StateRequest.SUCCESS, "The user has been activated"));
        return ResponseEntity.status(400).body(new ResponseDTO<>(StateRequest.ERROR, "The user could not be activated"));

    }
}
