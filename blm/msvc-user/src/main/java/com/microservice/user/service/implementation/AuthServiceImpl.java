package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.RoleEntity;
import com.microservice.user.persitence.model.entities.UserEntity;

import com.microservice.user.persitence.repository.UserRepository;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.AuthService;
import com.microservice.user.utils.AppUtil;
import com.microservice.user.utils.SpringSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppUtil appUtil;
    private final RolesService rolesService;
    private final SpringSecurityUtils springSecurityUtils;



    @Override
    public ResponseEntity<TokenDTO> register(UserDTO user) {

        //verify if the DTO is null
        if (user == null) return ResponseEntity.badRequest().body(new TokenDTO("User cannot be null"));

        try {

            //verify if the user already exist in the database
            if(appUtil.checkEmail(user.email()))  return ResponseEntity.badRequest().body(new TokenDTO("Email is already in use"));

//            check if the user you are trying to create another user has the required authorization
            if(!springSecurityUtils.canYouCreteRole(user.role())) return ResponseEntity.badRequest().body(new TokenDTO("Unauthorized"));

            //create a new role for the user
            RoleEntity newRole = rolesService.createRoleDefaultPermission(user.role());

            //create a new user
           UserEntity newUser =  UserEntity.builder()
                    .email(user.email())
                    .password(passwordEncoder.encode(user.password()))
                    .credentialsNonExpired(true)
                    .isEnabled(true)
                    .firstName(user.firstName())
                    .lastName(user.lastName())
                    .phoneNumber(user.phoneNumber()).build();

           newUser.getRoles().add(newRole);

           //save a new user
           userRepository.save(newUser);
            //generate and return user token

            return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(newUser)));


        } catch (Exception e) {
            return ResponseEntity.status(500).body(new TokenDTO("An unexpected error occurred"));
        }

    }

    @Override
    public ResponseEntity<TokenDTO> login(LoginDTO loginDTO) {

        if(loginDTO == null) return ResponseEntity.badRequest().body(new TokenDTO("User cannot be null"));
        if (loginDTO.email().isBlank() || loginDTO.password().isBlank()) return ResponseEntity.badRequest().body(new TokenDTO("Invalid username or password"));

        UserEntity userFound = userRepository.findByEmail(loginDTO.email()).orElseThrow(()-> new IllegalArgumentException("User not found"));

        if(!passwordEncoder.matches(loginDTO.password(), userFound.getPassword())) {
            return ResponseEntity.status(401).body(new TokenDTO("Wrong password"));
        }

        return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(userFound)));
    }

    @Override
    public TokenDTO refreshToken(String token) {
        return null;
    }
}
