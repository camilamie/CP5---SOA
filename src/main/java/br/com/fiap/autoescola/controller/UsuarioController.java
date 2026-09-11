package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;


    @PostMapping
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados,
                                                              UriComponentsBuilder uriBuilder) {
        var usuario = service.cadastrar(dados);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(
            @PageableDefault(size = 10, sort = {"login"}) Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<DadosDetalhamentoUsuario> atualizarPerfil(@PathVariable Long id,
                                                                    @RequestBody @Valid DadosAtualizacaoPerfil dados) {
        return ResponseEntity.ok(service.atualizarPerfil(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id,
                                        @AuthenticationPrincipal Usuario usuarioLogado) {
        service.excluir(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/senha")
    public ResponseEntity<Void> alterarPropriaSenha(@RequestBody @Valid DadosAlteracaoSenha dados,
                                                    @AuthenticationPrincipal Usuario usuarioLogado) {
        service.alterarSenha(usuarioLogado, dados);
        return ResponseEntity.noContent().build();
    }
}