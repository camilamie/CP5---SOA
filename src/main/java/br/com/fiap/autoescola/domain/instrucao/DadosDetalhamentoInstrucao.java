package br.com.fiap.autoescola.domain.instrucao;

import java.time.LocalDateTime;

public record DadosDetalhamentoInstrucao(
        Long id,
        Long idAluno,
        String nomeAluno,
        Long idInstrutor,
        String nomeInstrutor,
        LocalDateTime data,
        MotivoCancelamento motivoCancelamento
) {

    public DadosDetalhamentoInstrucao(Instrucao instrucao) {
        this(instrucao.getId(),
                instrucao.getAluno().getId(),
                instrucao.getAluno().getNome(),
                instrucao.getInstrutor().getId(),
                instrucao.getInstrutor().getNome(),
                instrucao.getData(),
                instrucao.getMotivoCancelamento());
    }
}