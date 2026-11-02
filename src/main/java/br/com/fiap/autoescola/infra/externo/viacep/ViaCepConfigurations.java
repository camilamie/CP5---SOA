package br.com.fiap.autoescola.infra.externo.viacep;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ViaCepConfigurations {

    @Bean
    public RestClient viaCepRestClient(@Value("${api.viacep.url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}