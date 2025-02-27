package com.microservice.user.utils;

import com.microservice.user.persistence.model.entities.UserEntity;
import com.microservice.user.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class AppUtil {

    final UserRepository userRepository;
    @Value("${MAX_ATTEMPTS}")
    private int maxAttempts;

    @Value("${LOCKOUT_MINUTES}")
    private int lockoutMinutes;

    public AppUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * check if an email doesn't it in use.
     *
     * @param email the email to check.
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

    public void handleFailedLogin(UserEntity user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

        // Lock account if max attempts reached
        if (user.getFailedLoginAttempts() >= maxAttempts) {
            user.setAccountLocked(true);
            user.setLockoutUntil(LocalDateTime.now().plusMinutes(lockoutMinutes));
        }

        userRepository.save(user);
    }

    public boolean isAccountLocked(UserEntity user) {

        if (!user.isAccountLocked()) {
            return false;
        }

        // Verificar si han pasado los 15 minutos
        if (user.getLockoutUntil() != null && LocalDateTime.now().isAfter(user.getLockoutUntil())) {
            // Desbloquear la cuenta
            user.setAccountLocked(false);
            user.setFailedLoginAttempts(0);
            user.setLockoutUntil(null);


            userRepository.save(user);
            return false;
        }

        return true;
    }

//    // Método para convertir la entidad a DTO
//    private UserDTO convertToDTO(UserEntity user) {
//        return new UserDTO(Role., user.getLastName(), user.getLastName(), user.getEmail(),null, user.getPhoneNumber());
//    }




}
