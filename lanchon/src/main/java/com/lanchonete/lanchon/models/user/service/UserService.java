package com.lanchonete.lanchon.models.user.service;

import com.lanchonete.lanchon.exception.domain.EmailAlreadyExistsException;
import com.lanchonete.lanchon.exception.domain.InvalidPayloadException;
import com.lanchonete.lanchon.exception.domain.PasswordMismatchException;
import com.lanchonete.lanchon.exception.domain.UserNotFoundException;
import com.lanchonete.lanchon.models.user.dto.CreateUserDTO;
import com.lanchonete.lanchon.models.user.dto.UpdateUserDTO;
import com.lanchonete.lanchon.models.user.dto.UserResponseDTO;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.enums.Role;
import com.lanchonete.lanchon.models.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO userDTO) {
        if (!userDTO.password().equals(userDTO.confirmPassword())) {
            throw new PasswordMismatchException();
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new EmailAlreadyExistsException(userDTO.email());
        }

        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPasswordHash(userDTO.password());
        user.setRole(userDTO.role() != null ? userDTO.role() : Role.CLIENT);
        user.setActive(true);
        return userRepository.save(user);
    }

    public User updateUsuario(Long id, UpdateUserDTO userDTO) {
        User usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userDTO.name() != null) {
            usuarioExistente.setName(userDTO.name());
        }

        if (userDTO.email() != null) {
            userRepository.findByEmail(userDTO.email())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> {
                        throw new EmailAlreadyExistsException(userDTO.email());
                    });
            usuarioExistente.setEmail(userDTO.email());
        }
        if (userDTO.active() != null) {
            usuarioExistente.setActive(userDTO.active());
        }
        if (userDTO.role() != null) {
            usuarioExistente.setRole(userDTO.role());
        }
        return userRepository.save(usuarioExistente);
    }

    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new InvalidPayloadException("O identificador do usuario e obrigatorio");
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();
    }
}
