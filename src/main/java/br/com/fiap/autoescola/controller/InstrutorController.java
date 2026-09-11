package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.domain.instrutor.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {

    @Autowired
    private InstrutorRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoInstrutor> cadastrar(@RequestBody @Valid DadosCadastroInstrutor dados,
                                                                UriComponentsBuilder uriBuilder) {
        var instrutor = new Instrutor(dados);
        repository.save(instrutor);

        var uri = uriBuilder.path("/instrutores/{id}").buildAndExpand(instrutor.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoInstrutor(instrutor));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemInstrutor>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var pagina = repository.findAllByAtivoTrue(paginacao).map(DadosListagemInstrutor::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoInstrutor> detalhar(@PathVariable Long id) {
        var instrutor = buscar(id);
        return ResponseEntity.ok(new DadosDetalhamentoInstrutor(instrutor));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoInstrutor> atualizar(@PathVariable Long id,
                                                                @RequestBody @Valid DadosAtualizacaoInstrutor dados) {
        var instrutor = buscar(id);
        instrutor.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoInstrutor(instrutor));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        var instrutor = buscar(id);
        instrutor.excluir();
        return ResponseEntity.noContent().build();
    }

    private Instrutor buscar(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}