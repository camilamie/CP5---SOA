package br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.aluno.AlunoRepository;
import br.com.fiap.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorAlunoAtivo implements ValidadorAgendamentoDeInstrucao {

    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        var alunoAtivo = alunoRepository.findAtivoById(dados.idAluno());

        if (!Boolean.TRUE.equals(alunoAtivo)) {
            throw new ValidacaoException("Não é possível agendar instrução para aluno inativo");
        }
    }}