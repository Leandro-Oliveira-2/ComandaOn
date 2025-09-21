package com.lanchonete.lanchon.models.user.dto;

import com.lanchonete.lanchon.models.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String confirmPassword,
        @NotNull Role role
) { }
