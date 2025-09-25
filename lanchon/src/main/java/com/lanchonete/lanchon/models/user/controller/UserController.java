package com.lanchonete.lanchon.models.user.controller;

import com.lanchonete.lanchon.models.user.dto.CreateUserDTO;
import com.lanchonete.lanchon.models.user.dto.UpdateUserDTO;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping("/create")
    public void createUser(@RequestBody CreateUserDTO user) {
        userService.createUser(user);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO userToUpdate) {
        try {
          User userUpdate =   userService.updateUsuario(id, userToUpdate);
            return ResponseEntity.ok(userUpdate);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable Long userId) throws Exception {
        userService.deleteUser(userId);
    }
}
