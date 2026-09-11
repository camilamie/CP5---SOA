package br.com.fiap.autoescola.domain.instrucao.validacoes.cancelamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.instrucao.Instrucao;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrucaoJaCancelada implements ValidadorCancelamentoDeInstrucao {

    @Override
    public void validar(Instrucao instrucao) {
        if (instrucao.isCancelada()) {
            throw new ValidacaoException("Essa instrução já foi cancelada");
        }
    }
}