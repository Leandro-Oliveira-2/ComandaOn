package com.lanchonete.lanchon.models.user.repository;

import com.lanchonete.lanchon.models.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findByName(String name);
    Optional<User> findByEmail(String email);
}
