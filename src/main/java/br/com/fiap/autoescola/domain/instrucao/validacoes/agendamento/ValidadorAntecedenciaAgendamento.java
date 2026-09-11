package br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaAgendamento implements ValidadorAgendamentoDeInstrucao {

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var agora = LocalDateTime.now();
        var minutosDeAntecedencia = Duration.between(agora, dados.data()).toMinutes();

        if (minutosDeAntecedencia < 30) {
            throw new ValidacaoException("A instrução deve ser agendada com antecedência mínima de 30 minutos");
        }
    }
}