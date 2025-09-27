package com.lanchonete.lanchon.models.user.service;

import com.lanchonete.lanchon.models.user.dto.CreateUserDTO;
import com.lanchonete.lanchon.models.user.dto.UpdateUserDTO;
import com.lanchonete.lanchon.models.user.dto.UserResponseDTO;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.enums.Role;
import com.lanchonete.lanchon.models.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO userDTO) {
        if (!userDTO.password().equals(userDTO.confirmPassword())) {
            throw new IllegalArgumentException("As senhas nao conferem");
        }

        String existEmail = String.valueOf(this.userRepository.findByEmail(userDTO.email()));

        if (existEmail.equals(userDTO.email())) {
            throw new IllegalArgumentException("email already exists");
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
        System.out.println("Id: " + id);
        System.out.println("User: " + userDTO);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User usuarioExistente = user.get();

            if (userDTO.name()!= null){
                usuarioExistente.setName(userDTO.name());
            }

            if (userDTO.email()!= null){
                usuarioExistente.setEmail(userDTO.email());
            }
            if (userDTO.active() != null){
                usuarioExistente.setActive(userDTO.active());
            }
            if (userDTO.role() != null){
                usuarioExistente.setRole(userDTO.role());
            }
            return userRepository.save(usuarioExistente);
        } else {
            return null;
        }
    }

    public void deleteUser(Long userId) throws Exception {
        if (userId == null) {
            throw new Exception("userId must not be null");
        }
        if (!userRepository.existsById(userId)) {
            throw new Exception("Id " + userId + " doesn't exist");
        }
        userRepository.deleteById(userId);
    }

    public List<UserResponseDTO> findAll() { // <-- Linha 1
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user)) // <-- Linha 2
                .toList();
    }
}
