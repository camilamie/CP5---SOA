package br.com.fiap.autoescola.domain.instrutor;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.endereco.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Instrutor")
@Table(name = "instrutores")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String telefone;

    @Column(unique = true)
    private String cnh;

    @Enumerated(EnumType.STRING)
    private br.com.fiap.autoescola.domain.instrutor.Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    private Boolean ativo;

    public Instrutor(DadosCadastroInstrutor dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cnh = dados.cnh();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(DadosAtualizacaoInstrutor dados) {
        if (dados.email() != null && !dados.email().equals(this.email)) {
            throw new ValidacaoException("Não é permitido alterar o e-mail do instrutor");
        }
        if (dados.cnh() != null && !dados.cnh().equals(this.cnh)) {
            throw new ValidacaoException("Não é permitido alterar a CNH do instrutor");
        }
        if (dados.especialidade() != null && dados.especialidade() != this.especialidade) {
            throw new ValidacaoException("Não é permitido alterar a especialidade do instrutor");
        }

        if (dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome();
        }
        if (dados.telefone() != null && !dados.telefone().isBlank()) {
            this.telefone = dados.telefone();
        }
        if (dados.endereco() != null) {
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
