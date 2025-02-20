package com.microservice.user.service.implementation;
import com.microservice.user.persitence.model.entities.RoleEntity;
import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.persitence.repository.RoleRepository;
import com.microservice.user.persitence.repository.UserRepository;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.UserService;
import com.microservice.user.utils.AppUtil;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final AppUtil appUtil;
    UserRepository userRepository;
    RoleRepository roleRepository;

    public UserServiceImpl(AppUtil appUtil) {
        this.appUtil = appUtil;
    }


    @Override
    public ResponseDTO updateUser(UserDTO userDTO) {

        try {
            // Validación inicial
            if (userDTO == null) {
                throw new IllegalArgumentException("El objeto userDTO no puede ser nulo.");
            }
            if (userDTO.email() == null) {
                throw new IllegalArgumentException("El ID del usuario es obligatorio para la actualización.");
            }

            // Buscar el usuario en la base de datos
            UserEntity existingUser = userRepository.findByEmail(userDTO.email()).orElseThrow( ()-> new IllegalArgumentException("El usuario no existe"));

            if (existingUser != null) {
                return new ResponseDTO(StateRequest.ERROR, "Usuario no encontrado");
            }

            // Actualizar solo los campos que se necesitan
            existingUser.setUserName(userDTO.firstName());
            existingUser.setEmail(userDTO.email());
            existingUser.setPhoneNumber(userDTO.phoneNumber());
            existingUser.setUpdatedAt(LocalDateTime.now());

            // Guardar cambios en la base de datos
            userRepository.save(existingUser);

            // Convertir a DTO y responder
            return new ResponseDTO(StateRequest.SUCCESS, "Usuario actualizado con éxito");

        } catch (IllegalArgumentException e) {
            return new ResponseDTO(StateRequest.ERROR, "Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(StateRequest.ERROR, "Error al actualizar el usuario");
        }
    }

    @Override
    public StateRequest deleteUser(String email) {

        if (!email.isBlank()){

            UserEntity userFound = userRepository.findByEmail(email).orElseThrow( ()-> new IllegalArgumentException("El usuario no existe"));

            userFound.setEnabled(false);
            userFound.setUpdatedAt(LocalDateTime.now());
            userRepository.save(userFound);

            return StateRequest.SUCCESS;

        }
        return StateRequest.ERROR;
    }

    @Override
    public UserDTO addRoleToUser(String email) {
        return null;
    }

    @Override
    public StateRequest addRoleToUser(String email, String nameRole) {

        try {
            UserEntity userFound = userRepository.findByEmail(email).orElseThrow( ()-> new IllegalArgumentException("El usuario no existe"));
            RoleEntity role = roleRepository.findByName(nameRole).orElseThrow( ()-> new IllegalArgumentException("El Rol no existe"));

            userFound.getRoles().add(role);
            userRepository.save(userFound);

            return StateRequest.SUCCESS;
        }catch (IllegalArgumentException e) {
            return StateRequest.ERROR;
        }


    }

    @Override
    public List<UserDTO> getUserByRol(String email, String rolName) {
        return List.of();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }

    @Override
    public StateRequest activateUser(String email) {
        return null;
    }
}
