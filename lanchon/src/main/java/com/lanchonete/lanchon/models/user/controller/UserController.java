package com.lanchonete.lanchon.models.user.controller;

import com.lanchonete.lanchon.models.user.dto.CreateUserDTO;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody CreateUserDTO user) {
        userService.createUser(user);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable Long userId) throws Exception {
        userService.deleteUser(userId);
    }
}
