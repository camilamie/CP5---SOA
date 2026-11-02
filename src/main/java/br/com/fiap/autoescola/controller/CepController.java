package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.domain.endereco.DadosEndereco;
import br.com.fiap.autoescola.infra.externo.viacep.ViaCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cep")
@Tag(name = "CEP (ViaCEP)", description = "Consulta de endereço em API externa")
public class CepController {

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping("/{cep}")
    @Operation(summary = "Consulta um endereço pelo CEP",
            description = "Consome a API pública ViaCEP e devolve o endereço no mesmo formato usado no cadastro.")
    public ResponseEntity<DadosEndereco> buscar(@PathVariable String cep) {
        return ResponseEntity.ok(viaCepService.buscarEndereco(cep));
    }
}