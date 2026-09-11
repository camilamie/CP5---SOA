package br.com.fiap.autoescola.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPerfil(

        @NotNull(message = "Perfil é obrigatório: ADMIN ou USER")
        Perfil perfil
) {
}