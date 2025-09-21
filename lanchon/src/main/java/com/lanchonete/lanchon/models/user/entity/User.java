package com.lanchonete.lanchon.models.user.entity;

import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(
  name = "users",
  uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @NotBlank
    @Column(nullable = false, length = 250)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, length = 250, unique = true)
    private String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 250)
    private String passwordHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false)
    private boolean active;

}
