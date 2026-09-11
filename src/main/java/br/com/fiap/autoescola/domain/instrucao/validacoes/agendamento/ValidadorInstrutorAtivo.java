package br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap.autoescola.domain.instrutor.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrutorAtivo implements ValidadorAgendamentoDeInstrucao {

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() == null) {
            return;
        }

        var instrutorAtivo = instrutorRepository.findAtivoById(dados.idInstrutor());

        if (!Boolean.TRUE.equals(instrutorAtivo)) {
            throw new ValidacaoException("Não é possível agendar instrução com instrutor inativo");
        }
    }
}