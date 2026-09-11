package br.com.fiap.autoescola.domain.instrucao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {

    @Query("""
            select count(i) from Instrucao i
            where i.aluno.id = :idAluno
            and i.data >= :inicioDoDia
            and i.data < :fimDoDia
            and i.motivoCancelamento is null
            """)
    long contarInstrucoesDoAlunoNoDia(@Param("idAluno") Long idAluno,
                                      @Param("inicioDoDia") LocalDateTime inicioDoDia,
                                      @Param("fimDoDia") LocalDateTime fimDoDia);

    @Query("""
            select count(i) from Instrucao i
            where i.instrutor.id = :idInstrutor
            and i.data > :inicio
            and i.data < :fim
            and i.motivoCancelamento is null
            """)
    long contarInstrucoesDoInstrutorNoHorario(@Param("idInstrutor") Long idInstrutor,
                                              @Param("inicio") LocalDateTime inicio,
                                              @Param("fim") LocalDateTime fim);
}