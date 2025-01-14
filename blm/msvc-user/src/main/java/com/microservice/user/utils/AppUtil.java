package com.microservice.user.utils;

import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.repository.UserRepository;
import com.microservice.user.presentation.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppUtil {

   final UserRepository userRepository;

    /**
     * check if an email doesn't it in use.
     *
     * @param email  the email to check.
     * @return true if the email not exist, false if the email already exist .
     */
    public boolean checkEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío");
        }

        try {
            Optional<UserEntity> emailOptional = userRepository.findByEmail(email);
            return emailOptional.isPresent();

        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el correo en la base de datos", e);
        }
    }



}
