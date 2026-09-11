package br.com.fiap.autoescola.domain.instrutor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {

    Page<Instrutor> findAllByAtivoTrue(Pageable paginacao);

    @Query("select i.ativo from Instrutor i where i.id = :id")
    Boolean findAtivoById(@Param("id") Long id);

    @Query("""
            select i from Instrutor i
            where i.ativo = true
            and i.id not in (
                select ins.instrutor.id from Instrucao ins
                where ins.data > :inicio
                and ins.data < :fim
                and ins.motivoCancelamento is null
            )
            """)
    List<Instrutor> buscarInstrutoresLivres(@Param("inicio") LocalDateTime inicio,
                                            @Param("fim") LocalDateTime fim);
}