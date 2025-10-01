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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserDTO userDTO) {
        if (!userDTO.password().equals(userDTO.confirmPassword())) {
            throw new PasswordMismatchException();
        }

      final String email = userDTO.email().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        final String hash = passwordEncoder.encode(userDTO.password());
        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(email);
        user.setPasswordHash(hash);
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
