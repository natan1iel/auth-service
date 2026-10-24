package com.volei.auth.exception;

import org.springframework.http.HttpStatus;

public class RegraDeNegocioException extends RuntimeException {
    private static final String FALHA_LOGIN = "E-mail ou senha inválidos";

    private final HttpStatus status;
    private final String codigo;

    private RegraDeNegocioException(HttpStatus status, String codigo, String mensagem) {
        super(mensagem);
        this.status = status;
        this.codigo = codigo;
    }

    public static RegraDeNegocioException invalida(String mensagem) {
        return new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS", mensagem);
    }

    public static RegraDeNegocioException credenciaisInvalidas() {
        return new RegraDeNegocioException(HttpStatus.UNAUTHORIZED, "CREDENCIAIS_INVALIDAS", FALHA_LOGIN);
    }

    public static RegraDeNegocioException emailJaCadastrado(String email) {
        return new RegraDeNegocioException(HttpStatus.CONFLICT, "EMAIL_JA_CADASTRADO",
                "Já existe uma conta com o e-mail " + email);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCodigo() {
        return codigo;
    }
}
