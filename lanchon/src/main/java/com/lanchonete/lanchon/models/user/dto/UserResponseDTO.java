package com.lanchonete.lanchon.models.user.dto;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.enums.Role;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role,
        boolean active
) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.isActive());
    }
}
