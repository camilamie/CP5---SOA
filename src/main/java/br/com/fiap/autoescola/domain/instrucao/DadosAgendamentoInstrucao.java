package br.com.fiap.autoescola.domain.instrucao;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoInstrucao(

        @NotNull(message = "O aluno é obrigatório")
        Long idAluno,

        Long idInstrutor,

        @NotNull(message = "A data/hora é obrigatória")
        @Future(message = "A data/hora deve estar no futuro")
        LocalDateTime data
) {
}