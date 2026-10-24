package com.volei.auth.service;

import com.volei.auth.dto.LoginRequest;
import com.volei.auth.dto.RegistroRequest;
import com.volei.auth.dto.TokenResponse;
import com.volei.auth.dto.UsuarioResponse;
import com.volei.auth.exception.RegraDeNegocioException;
import com.volei.auth.model.Papel;
import com.volei.auth.model.Usuario;
import com.volei.auth.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public UsuarioResponse registrar(RegistroRequest requisicao) {
        return UsuarioResponse.de(criarUsuario(requisicao.nome(), requisicao.email(), requisicao.senha(), Papel.USUARIO));
    }

    @Transactional
    public Usuario criarUsuario(String nome, String email, String senha, Papel papel) {
        if (nome == null || nome.isBlank()) {
            throw RegraDeNegocioException.invalida("O nome é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw RegraDeNegocioException.invalida("O e-mail é obrigatório");
        }
        if (senha == null || senha.length() < 6) {
            throw RegraDeNegocioException.invalida("A senha precisa ter ao menos 6 caracteres");
        }

        String emailNormalizado = email.trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw RegraDeNegocioException.emailJaCadastrado(emailNormalizado);
        }

        String hash = codificador.encode(senha);

        return usuarioRepository.save(new Usuario(nome.trim(), emailNormalizado, hash, papel));
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest requisicao) {
        if (requisicao.email() == null || requisicao.senha() == null) {
            throw RegraDeNegocioException.credenciaisInvalidas();
        }

        Optional<Usuario> encontrado =
                usuarioRepository.findByEmailIgnoreCase(requisicao.email().trim());

        if (encontrado.isEmpty()) {
            throw RegraDeNegocioException.credenciaisInvalidas();
        }

        Usuario usuario = encontrado.get();

        if (!codificador.matches(requisicao.senha(), usuario.getSenhaHash())) {
            throw RegraDeNegocioException.credenciaisInvalidas();
        }

        return new TokenResponse(tokenService.gerar(usuario));
    }
}
