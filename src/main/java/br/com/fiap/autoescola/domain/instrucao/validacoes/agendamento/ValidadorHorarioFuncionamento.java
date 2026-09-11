package br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamentoDeInstrucao {

    private static final LocalTime ABERTURA = LocalTime.of(6, 0);
    // A autoescola fecha às 21:00 e a instrução dura 1 hora
    private static final LocalTime ULTIMO_INICIO = LocalTime.of(20, 0);

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var data = dados.data();

        var domingo = data.getDayOfWeek() == DayOfWeek.SUNDAY;
        var antesDaAbertura = data.toLocalTime().isBefore(ABERTURA);
        var depoisDoUltimoInicio = data.toLocalTime().isAfter(ULTIMO_INICIO);

        if (domingo || antesDaAbertura || depoisDoUltimoInicio) {
            throw new ValidacaoException(
                    "Instrução fora do horário de funcionamento: segunda a sábado, das 06:00 às 21:00 " +
                            "(O último horário de início é 20:00)");
        }
    }
}