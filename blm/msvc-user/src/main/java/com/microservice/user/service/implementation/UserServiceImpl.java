package com.microservice.user.service.implementation;
import com.microservice.user.persitence.model.entities.RoleEntity;
import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.persitence.model.enums.Status;
import com.microservice.user.persitence.repository.RoleRepository;
import com.microservice.user.persitence.repository.UserRepository;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.UserService;
import com.microservice.user.utils.AppUtil;
import com.microservice.user.utils.SpringSecurityUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service

public class UserServiceImpl implements UserService {

    private final AppUtil appUtil;
    UserRepository userRepository;
    RoleRepository roleRepository;
    SpringSecurityUtils springSecurityUtils;

    public UserServiceImpl(AppUtil appUtil, UserRepository userRepository, RoleRepository roleRepository) {
        this.appUtil = appUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

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

            if(!springSecurityUtils.canManageThisUser(userFound.getRoles().stream().toList())) throw new IllegalArgumentException("the user doesn't have the authority to do this action");

            userFound.setEnabled(false);
            userFound.setStatus(Status.INACTIVE);
            userFound.setUpdatedAt(LocalDateTime.now());
            userRepository.save(userFound);

            return StateRequest.SUCCESS;

        }
        return StateRequest.ERROR;
    }

    @Override
    public StateRequest addRoleToUser(String email, String nameRole) {

        try {
            UserEntity userFound = userRepository.findByEmail(email).orElseThrow( ()-> new IllegalArgumentException("El usuario no existe"));

            if(springSecurityUtils.canManageThisUser(userFound.getRoles().stream().toList())) throw new IllegalArgumentException("the user doesn't have the authority to do this action");

            RoleEntity role = roleRepository.findByName(nameRole).orElseThrow( ()-> new IllegalArgumentException("El Rol no existe"));

            userFound.getRoles().add(role);
            userRepository.save(userFound);

            return StateRequest.SUCCESS;
        }catch (IllegalArgumentException e) {
            return StateRequest.ERROR;
        }


    }

    @Override
    public StateRequest removeRoleFromUser(String email, String nameRole) {
       try {

           UserEntity userFound = userRepository.findByEmail(email).orElseThrow( ()-> new IllegalArgumentException("El usuario no existe"));
           if (!userFound.isEnabled() && userFound.getStatus() != Status.ACTIVE) throw new IllegalArgumentException("El usuario no está activo");
           if(springSecurityUtils.canManageThisUser(userFound.getRoles().stream().toList())) throw new IllegalArgumentException("the user doesn't have the authority to do this action");
           userFound.getRoles().removeIf(role -> role.getName().equals(nameRole));

           userRepository.save(userFound);
           return StateRequest.SUCCESS;

       }catch (Exception e){
           return StateRequest.ERROR;
       }

    }

    @Override
    public List<UserEntity> getAllUsers(Status status) {
        Status effectiveStatus = (status != null) ? status : Status.ACTIVE;

        try {
            return  userRepository.findByUserStatus(effectiveStatus);

        }catch (Exception e) {
            throw new RuntimeException("Error al obtener los usuarios");
        }

    }

    @Override
    public List<UserEntity> getUserByRol(String rolName) {
        try {

            return userRepository.findActiveUsersByRole(rolName);

        }catch (Exception e) {
            throw new RuntimeException("Error al obtener los usuarios");
        }
    }

    @Override
    public StateRequest activateUser(String email) {
        try {
            UserEntity userFound = userRepository.findByEmail(email).orElseThrow( ()-> new IllegalArgumentException("El usuario no existe"));
            userFound.setEnabled(true);
            userFound.setStatus(Status.ACTIVE);
            userRepository.save(userFound);

            return StateRequest.SUCCESS;
        }catch (Exception e) {
            return StateRequest.ERROR;
        }
    }

}
