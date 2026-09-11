package br.com.fiap.autoescola.domain.instrucao.validacoes.cancelamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.instrucao.Instrucao;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaCancelamento implements ValidadorCancelamentoDeInstrucao {

    @Override
    public void validar(Instrucao instrucao) {
        var limiteParaCancelar = instrucao.getData().minusHours(24);

        if (LocalDateTime.now().isAfter(limiteParaCancelar)) {
            throw new ValidacaoException("A instrução só pode ser cancelada com antecedência mínima de 24 horas");
        }
    }
}