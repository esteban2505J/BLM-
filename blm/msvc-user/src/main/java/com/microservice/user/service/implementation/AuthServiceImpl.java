package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.RoleEntity;
import com.microservice.user.persitence.model.entities.UserEntity;

import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.persitence.model.enums.Status;
import com.microservice.user.persitence.model.vo.TokenEntity;
import com.microservice.user.persitence.repository.UserRepository;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.AuthService;
import com.microservice.user.utils.AppUtil;
import com.microservice.user.utils.EmailUtil;
import com.microservice.user.utils.SpringSecurityUtils;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppUtil appUtil;
    private final RolesService rolesService;
    private final SpringSecurityUtils springSecurityUtils;
    private final EmailUtil emailUtil;



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

            // **Generar Tokens**
            TokenEntity accessToken = jwtService.generateToken(newUser);
            TokenEntity refreshToken = jwtService.generateRefreshToken(newUser);

            // **Crear entidades de tokens y asociarlas al usuario**
            List<TokenEntity> tokens = List.of(
                    accessToken,refreshToken
            );

            newUser.getTokens().addAll(tokens);
            newUser.getRoles().add(newRole);

           //save a new user
           userRepository.save(newUser);

            //generate and return user token
            return new TokenDTO(accessToken.getToken());

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


        TokenEntity accessToken = jwtService.generateToken(userFound);
        TokenEntity refreshToken = jwtService.generateRefreshToken(userFound);

        userFound.getTokens().clear();

        userFound.getTokens().add(accessToken);
        userFound.getTokens().add(refreshToken);

        userRepository.updateTokens(userFound.getId(),List.of(accessToken,refreshToken));

        return new TokenDTO(accessToken.getToken());
    }

    @Override
    public boolean checkToken(TokenDTO token) {
        if(token == null || token.token().isBlank())throw new InsufficientAuthenticationException("Token cannot be empty");
        try {
            return jwtService.validateToken(token.token());
        }catch (JwtException e) {
            return false;
        }
    }

    @Override
    public StateRequest forgotPassword(String email) {
        UserEntity userFound= userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));

        if (userFound.getStatus() != Status.ACTIVE) return StateRequest.ERROR;
        try {
            TokenEntity resetToken =  jwtService.generateToken(userFound);
            userFound.getTokens().clear();
            userFound.getTokens().add(resetToken);
            userRepository.updateTokens(userFound.getId(),List.of(resetToken));
            emailUtil.sendEmail(email,"Forgot password", resetToken.getToken());
            return StateRequest.SUCCESS;
        }catch (Exception e) {
            return StateRequest.ERROR;
        }

    }

    @Override
    public StateRequest checkTokenPassword(TokenDTO token , String email) {
        if (token.token().isBlank()) return StateRequest.ERROR;
        try{
           if(!jwtService.validateToken(token.token())) return StateRequest.ERROR;
           UserEntity userFound = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
           if(userFound.getStatus() != Status.ACTIVE) return StateRequest.ERROR;
           if(!userFound.getTokens().get(0).getToken().equals(token.token())) return StateRequest.ERROR;

           return StateRequest.SUCCESS;


        }catch (Exception e) {
            return StateRequest.ERROR;
        }
    }

    @Override
    public StateRequest resetPassword(String password, String email) {
       if(password.isBlank() || email.isBlank()) throw new IllegalArgumentException("Password and email cannot be empty");
       UserEntity userFound = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
       if(userFound.getStatus() != Status.ACTIVE) throw  new IllegalStateException("User is not active");

       userRepository.updatePassword(userFound.getId(),passwordEncoder.encode(password));

       return StateRequest.SUCCESS;
    }

}
