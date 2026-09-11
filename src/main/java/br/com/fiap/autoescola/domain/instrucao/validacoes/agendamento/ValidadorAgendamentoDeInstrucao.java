package br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento;

import br.com.fiap.autoescola.domain.instrucao.DadosAgendamentoInstrucao;

public interface ValidadorAgendamentoDeInstrucao {

    void validar(DadosAgendamentoInstrucao dados);
}