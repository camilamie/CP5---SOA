package br.com.fiap.autoescola.domain.instrucao;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.aluno.AlunoRepository;
import br.com.fiap.autoescola.domain.instrucao.validacoes.agendamento.ValidadorAgendamentoDeInstrucao;
import br.com.fiap.autoescola.domain.instrucao.validacoes.cancelamento.ValidadorCancelamentoDeInstrucao;
import br.com.fiap.autoescola.domain.instrutor.Instrutor;
import br.com.fiap.autoescola.domain.instrutor.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AgendaDeInstrucoes {

    @Autowired
    private InstrucaoRepository instrucaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private List<ValidadorAgendamentoDeInstrucao> validadoresAgendamento;

    @Autowired
    private List<ValidadorCancelamentoDeInstrucao> validadoresCancelamento;

    @Transactional
    public DadosDetalhamentoInstrucao agendar(DadosAgendamentoInstrucao dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new ValidacaoException("Aluno não encontrado");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())) {
            throw new ValidacaoException("Instrutor não encontrado");
        }

        validadoresAgendamento.forEach(validador -> validador.validar(dados));

        var aluno = alunoRepository.getReferenceById(dados.idAluno());
        var instrutor = escolherInstrutor(dados);

        var instrucao = new Instrucao(aluno, instrutor, dados.data());
        instrucaoRepository.save(instrucao);

        return new DadosDetalhamentoInstrucao(instrucao);
    }

    @Transactional
    public void cancelar(DadosCancelamentoInstrucao dados) {
        var instrucao = instrucaoRepository.findById(dados.idInstrucao())
                .orElseThrow(() -> new ValidacaoException("Instrução não encontrada"));

        validadoresCancelamento.forEach(validador -> validador.validar(instrucao));

        instrucao.cancelar(dados.motivo());
    }

    private Instrutor escolherInstrutor(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }

        var inicio = dados.data().minusHours(1);
        var fim = dados.data().plusHours(1);
        var instrutoresLivres = instrutorRepository.buscarInstrutoresLivres(inicio, fim);

        if (instrutoresLivres.isEmpty()) {
            throw new ValidacaoException("Nenhum instrutor disponível nessa data/hora");
        }

        var indiceSorteado = ThreadLocalRandom.current().nextInt(instrutoresLivres.size());
        return instrutoresLivres.get(indiceSorteado);
    }
}