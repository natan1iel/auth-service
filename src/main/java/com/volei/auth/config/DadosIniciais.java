package com.volei.auth.config;

import com.volei.auth.model.Papel;
import com.volei.auth.repository.UsuarioRepository;
import com.volei.auth.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DadosIniciais implements CommandLineRunner {
    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    public DadosIniciais(AuthService authService, UsuarioRepository usuarioRepository) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        authService.criarUsuario("Administrador", "admin@volei.com", "admin123", Papel.ADMIN);
        authService.criarUsuario("João", "joao@volei.com", "joao123", Papel.USUARIO);
        authService.criarUsuario("Maria", "maria@volei.com", "maria123", Papel.USUARIO);
    }
}
