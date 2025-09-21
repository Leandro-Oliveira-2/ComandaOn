package com.lanchonete.lanchon.models.user.dto;

import com.lanchonete.lanchon.models.user.enums.Role;

public record UpdateUserDTO(
        String name,
        String email,
        Role role,
        Boolean active
) { }
