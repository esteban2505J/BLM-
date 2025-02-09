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
import lombok.AllArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppUtil appUtil;
    private final RolesService rolesService;
    private final SpringSecurityUtils springSecurityUtils;



    @Override
    public TokenDTO register(UserDTO user) {

        //verify if the DTO is null
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        try {

            //verify if the user already exist in the database
            if(appUtil.checkEmail(user.email()))  throw new IllegalArgumentException("Email is already in use");

//            check if the user you are trying to create another user has the required authorization
            if(!springSecurityUtils.canYouCreteRole(user.role())) throw new InsufficientAuthenticationException("You do not have the required role");

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
            String refreshToken =  jwtService.generateRefreshToken(newUser);
            String token = jwtService.generateToken(newUser);
           newUser.getTokens().add();

           //save a new user
           userRepository.save(newUser);

            //generate and return user token
            return new TokenDTO();


        } catch (Exception e) {
            return new TokenDTO("An unexpected error occurred");
        }

    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) {

        if(loginDTO == null) return new TokenDTO("User cannot be null");
        if (loginDTO.email().isBlank() || loginDTO.password().isBlank()) new TokenDTO("Invalid username or password");

        UserEntity userFound = userRepository.findByEmail(loginDTO.email()).orElseThrow(()-> new IllegalArgumentException("User not found"));

        if(!passwordEncoder.matches(loginDTO.password(), userFound.getPassword())) {
            return new TokenDTO("Wrong password");
        }

        return new TokenDTO(jwtService.generateToken(userFound));
    }


}
