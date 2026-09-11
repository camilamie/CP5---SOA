package br.com.fiap.autoescola.domain.instrutor;

import br.com.fiap.autoescola.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;

public record DadosAtualizacaoInstrutor(

        // Campos que podem ser atualizados
        String nome,
        String telefone,
        @Valid DadosEndereco endereco,

        // Campos que não podem ser atualizados.
        String email,
        String cnh,
        Especialidade especialidade
) {
}
