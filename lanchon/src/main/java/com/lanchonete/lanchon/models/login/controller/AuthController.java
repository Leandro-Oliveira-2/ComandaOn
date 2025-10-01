package com.lanchonete.lanchon.models.login.controller;


import com.lanchonete.lanchon.config.security.JwtService;
import com.lanchonete.lanchon.models.login.dto.AuthResponse;
import com.lanchonete.lanchon.models.login.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto body) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.email(), body.password())
        );
        String token = jwtService.generateToken(body.email());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
