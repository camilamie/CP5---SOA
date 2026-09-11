package br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap.autoescola.domain.instrucao.InstrucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrutorComOutraInstrucaoNoHorario implements ValidadorAgendamentoDeInstrucao {

    @Autowired
    private InstrucaoRepository instrucaoRepository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() == null) {
            return;
        }
        var inicio = dados.data().minusHours(1);
        var fim = dados.data().plusHours(1);

        var quantidade = instrucaoRepository.contarInstrucoesDoInstrutorNoHorario(dados.idInstrutor(), inicio, fim);

        if (quantidade > 0) {
            throw new ValidacaoException("O instrutor já possui outra instrução agendada nesse horário");
        }
    }
}