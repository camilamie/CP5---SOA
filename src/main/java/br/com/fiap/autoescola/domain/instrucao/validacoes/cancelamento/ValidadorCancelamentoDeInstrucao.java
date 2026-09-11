package br.com.fiap.autoescola.domain.instrucao.validacoes.cancelamento;

import br.com.fiap.autoescola.domain.instrucao.Instrucao;

public interface ValidadorCancelamentoDeInstrucao {

    void validar(Instrucao instrucao);
}