package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.ApiIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InstrutorControllerTest extends ApiIntegrationTest {

    private String jsonInstrutor(String especialidade) {
        return """
                {"nome": "João Souza", "email": "joao@email.com", "telefone": "11988887777",
                 "cnh": "12345678901", "especialidade": "%s", "endereco": %s}
                """.formatted(especialidade, jsonEndereco());
    }

    @Test
    @DisplayName("Deve cadastrar instrutor e devolver 201")
    void cadastrarComSucesso() throws Exception {
        mvc.perform(post("/instrutores")
                        .header("Authorization", tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInstrutor("CARROS")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("João Souza"))
                .andExpect(jsonPath("$.especialidade").value("CARROS"));
    }

    @Test
    @DisplayName("Deve devolver 400 quando a especialidade não existir")
    void cadastrarComEspecialidadeInvalida() throws Exception {
        mvc.perform(post("/instrutores")
                        .header("Authorization", tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInstrutor("AVIAO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve devolver 404 ao detalhar instrutor inexistente")
    void detalharInexistente() throws Exception {
        mvc.perform(get("/instrutores/{id}", 999_999).header("Authorization", tokenAdmin()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Registro não encontrado"));
    }

    @Test
    @DisplayName("Deve listar instrutores de forma paginada")
    void listar() throws Exception {
        var token = tokenAdmin();
        mvc.perform(post("/instrutores")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInstrutor("MOTOS")));

        mvc.perform(get("/instrutores").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("João Souza"));
    }
}