package br.com.fiap.autoescola.domain.aluno;

import br.com.fiap.autoescola.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;

public record DadosAtualizacaoAluno(

        // Campos que podem ser atualizados
        String nome,
        String telefone,
        @Valid DadosEndereco endereco,

        // Campos que não podem ser atualizados
        String email,
        String cpf
) {
}