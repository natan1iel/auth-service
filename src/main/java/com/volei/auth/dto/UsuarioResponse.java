package com.volei.auth.dto;

import com.volei.auth.model.Usuario;

public record UsuarioResponse(Long id, String nome, String email, String papel) {
    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPapel().name());
    }
}
