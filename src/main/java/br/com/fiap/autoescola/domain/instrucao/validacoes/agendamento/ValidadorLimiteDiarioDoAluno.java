package br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap.autoescola.domain.instrucao.InstrucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorLimiteDiarioDoAluno implements ValidadorAgendamentoDeInstrucao {

    private static final int LIMITE_POR_DIA = 2;

    @Autowired
    private InstrucaoRepository instrucaoRepository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var inicioDoDia = dados.data().toLocalDate().atStartOfDay();
        var fimDoDia = inicioDoDia.plusDays(1);

        var quantidade = instrucaoRepository.contarInstrucoesDoAlunoNoDia(dados.idAluno(), inicioDoDia, fimDoDia);

        if (quantidade >= LIMITE_POR_DIA) {
            throw new ValidacaoException("O aluno já possui 2 instruções agendadas nesse dia");
        }
    }
}