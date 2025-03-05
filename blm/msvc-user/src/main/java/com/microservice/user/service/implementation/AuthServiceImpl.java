package com.microservice.user.service.implementation;

import com.microservice.user.persistence.model.entities.RoleEntity;
import com.microservice.user.persistence.model.entities.UserEntity;

import com.microservice.user.persistence.model.enums.StateRequest;
import com.microservice.user.persistence.model.enums.Status;
import com.microservice.user.persistence.model.vo.TokenEntity;
import com.microservice.user.persistence.repository.TokenRepository;
import com.microservice.user.persistence.repository.UserRepository;
import com.microservice.user.presentation.dtos.*;
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
    private final TokenRepository tokenRepository;



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
                   .status(Status.ACTIVE)
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

        // Check if account is locked
        if ( appUtil.isAccountLocked(userFound)) {
            throw new IllegalStateException("Account is temporarily locked. Try again later.");
        }


        if(!passwordEncoder.matches(loginDTO.password(), userFound.getPassword())) {
            appUtil.handleFailedLogin(userFound);
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
    public StateRequest resetPassword(LoginDTO loginDTO) {
       if(loginDTO.password().isBlank() || loginDTO.email().isBlank()) throw new IllegalArgumentException("Password and email cannot be empty");
       UserEntity       userFound = userRepository.findByEmail(loginDTO.email()).orElseThrow(()-> new IllegalArgumentException("User not found"));
       if(userFound.getStatus() != Status.ACTIVE) throw  new IllegalStateException("User is not active");

       userRepository.updatePassword(userFound.getId(),passwordEncoder.encode(loginDTO.password()));

       return StateRequest.SUCCESS;
    }

    @Override
    public StateRequest logout(String email) {
        try {

            if(email.isBlank()) throw new IllegalArgumentException("Email cannot be empty");
            UserEntity userFound = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
            if(userFound.getStatus() != Status.ACTIVE) throw  new IllegalStateException("User is not active");

            tokenRepository.deleteAllTokensByUserId(userFound.getId());
            return StateRequest.SUCCESS;


        }catch (Exception e) {
            return StateRequest.ERROR;
        }

    }

    @Override
    public ResponseDTO<TokenDTO> creteAnEmployee(EmployeeDTO employee) {

       try {

           if (employee == null) throw new IllegalArgumentException("Employee cannot be null");
           if (employee.employeeCode().isBlank() || employee.status() != Status.ACTIVE)throw new IllegalArgumentException("Employee code cannot be empty");
           //verify if the user already exist in the database
           if(appUtil.checkEmail(employee.email()))  throw new IllegalArgumentException("Email is already in use");

//            check if the user you are trying to create another user has the required authorization
           if(!springSecurityUtils.canManageThisUser(employee.roles().stream().toList())) throw new InsufficientAuthenticationException("You do not have the required role");

          UserEntity newUser =  UserEntity.builder()
                   .email(employee.email())
                   .firstName(employee.firstName())
                   .lastName(employee.lastName())
                   .userName(employee.userName())
                   .employeeCode(employee.employeeCode())
                   .branchIds(employee.branchIds())
                   .status(Status.ACTIVE)
                   .roles(employee.roles()).build();


           // **Generar Tokens**
           TokenEntity accessToken = jwtService.generateToken(newUser);
           TokenEntity refreshToken = jwtService.generateRefreshToken(newUser);

           // **Crear entidades de tokens y asociarlas al usuario**
           List<TokenEntity> tokens = List.of(
                   accessToken,refreshToken
           );

           newUser.getTokens().addAll(tokens);

           //save a new user
           userRepository.save(newUser);

           //generate and return user token
           return new ResponseDTO(StateRequest.SUCCESS, new TokenDTO(accessToken.getToken()));

       }catch (Exception e) {
           throw  new IllegalStateException("An error occurred while creating an employee");
       }
    }

}
