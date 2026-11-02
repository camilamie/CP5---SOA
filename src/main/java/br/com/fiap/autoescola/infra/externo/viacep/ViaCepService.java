package br.com.fiap.autoescola.infra.externo.viacep;

import br.com.fiap.autoescola.domain.ValidacaoException;
import br.com.fiap.autoescola.domain.endereco.DadosEndereco;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ViaCepService {

    private final RestClient viaCepRestClient;

    public ViaCepService(RestClient viaCepRestClient) {
        this.viaCepRestClient = viaCepRestClient;
    }

    public DadosEndereco buscarEndereco(String cep) {
        var cepSomenteNumeros = cep == null ? "" : cep.replaceAll("\\D", "");
        if (cepSomenteNumeros.length() != 8) {
            throw new ValidacaoException("CEP inválido: informe 8 dígitos");
        }

        DadosViaCep resposta;
        try {
            resposta = viaCepRestClient.get()
                    .uri("/{cep}/json/", cepSomenteNumeros)
                    .retrieve()
                    .body(DadosViaCep.class);
        } catch (RestClientException e) {
            throw new ValidacaoException("Não foi possível consultar o ViaCEP no momento. Tente novamente.");
        }

        // O ViaCEP responde 200 com {"erro": "true"} quando o CEP não existe
        if (resposta == null || resposta.erro() != null) {
            throw new ValidacaoException("CEP não encontrado");
        }

        return new DadosEndereco(resposta.logradouro(), null, resposta.complemento(),
                resposta.bairro(), resposta.localidade(), resposta.uf(), resposta.cep());
    }
}