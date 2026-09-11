package br.com.fiap.autoescola.domain.aluno;

public record DadosListagemAluno(
        Long id,
        String nome,
        String email,
        String cpf
) {

    public DadosListagemAluno(Aluno aluno) {
        this(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getCpf());
    }
}