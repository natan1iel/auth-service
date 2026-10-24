package com.volei.auth.controller;

import com.volei.auth.dto.LoginRequest;
import com.volei.auth.dto.RegistroRequest;
import com.volei.auth.dto.TokenResponse;
import com.volei.auth.dto.UsuarioResponse;
import com.volei.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody RegistroRequest requisicao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(requisicao));
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest requisicao) {
        return authService.login(requisicao);
    }
}
