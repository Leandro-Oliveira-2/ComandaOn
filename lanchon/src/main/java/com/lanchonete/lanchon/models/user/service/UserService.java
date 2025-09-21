package com.lanchonete.lanchon.models.user.service;

import com.lanchonete.lanchon.models.user.dto.CreateUserDTO;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.enums.Role;
import com.lanchonete.lanchon.models.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO userDTO) {
        if (!userDTO.password().equals(userDTO.confirmPassword())) {
            throw new IllegalArgumentException("As senhas não conferem");
        }

        String existEmail = String.valueOf(this.userRepository.findByEmail(userDTO.email()));

        if (existEmail.equals(userDTO.email())) {
            throw new IllegalArgumentException("email already exists");
        }

        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPasswordHash(passwordEncoder.encode(userDTO.password()));
        user.setRole(userDTO.role() != null ? userDTO.role() : Role.CLIENT);
        user.setActive(true);
        return userRepository.save(user);
    }

    public User UpdateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) throws Exception {
        if(userId == null) {
            throw new Exception("userId must not be null");
        }
        if(!userRepository.existsById(userId)){
            throw new Exception("Id " + userId + " doesn't exist");
        }
        userRepository.deleteById(userId);
    }

    public List<User> findAll() {
        User[] users = userRepository.findAll().toArray(new User[0]);
        return Arrays.asList(users);
    }

}
