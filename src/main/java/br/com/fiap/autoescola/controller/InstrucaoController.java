package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.domain.instrucao.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/instrucoes")
public class InstrucaoController {

    @Autowired
    private AgendaDeInstrucoes agenda;

    @Autowired
    private InstrucaoRepository repository;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoInstrucao> agendar(@RequestBody @Valid DadosAgendamentoInstrucao dados,
                                                              UriComponentsBuilder uriBuilder) {
        var instrucao = agenda.agendar(dados);
        var uri = uriBuilder.path("/instrucoes/{id}").buildAndExpand(instrucao.id()).toUri();
        return ResponseEntity.created(uri).body(instrucao);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoInstrucao>> listar(
            @PageableDefault(size = 10, sort = {"data"}) Pageable paginacao) {
        return ResponseEntity.ok(repository.findAll(paginacao).map(DadosDetalhamentoInstrucao::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoInstrucao> detalhar(@PathVariable Long id) {
        var instrucao = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok(new DadosDetalhamentoInstrucao(instrucao));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelar(@RequestBody @Valid DadosCancelamentoInstrucao dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}