package br.com.fiap.autoescola.domain.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(

        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        String numero,

        String complemento,

        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @NotBlank(message = "Cidade é obrigatória")
        String cidade,

        @NotBlank(message = "UF é obrigatória")
        String uf,

        @NotBlank(message = "CEP é obrigatório")
        String cep
) {
}
