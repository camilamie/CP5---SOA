package br.com.fiap.autoescola.domain.instrucao;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoInstrucao(

        @NotNull(message = "A instrução é obrigatória")
        Long idInstrucao,

        @NotNull(message = "O motivo do cancelamento é obrigatório: ALUNO_DESISTIU, INSTRUTOR_CANCELOU ou OUTROS")
        MotivoCancelamento motivo
) {
}