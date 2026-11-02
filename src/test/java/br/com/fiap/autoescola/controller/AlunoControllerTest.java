package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.ApiIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AlunoControllerTest extends ApiIntegrationTest {

    private String jsonAluno() {
        return """
                {"nome": "Maria Silva", "email": "maria@email.com", "telefone": "11999990000",
                 "cpf": "12345678900", "endereco": %s}
                """.formatted(jsonEndereco());
    }

    @Test
    @DisplayName("Deve cadastrar aluno e devolver 201 com header Location")
    void cadastrarComSucesso() throws Exception {
        mvc.perform(post("/alunos")
                        .header("Authorization", tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAluno()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.endereco.uf").value("SP"));
    }

    @Test
    @DisplayName("Deve devolver 400 quando campos obrigatórios não forem enviados")
    void cadastrarComDadosInvalidos() throws Exception {
        mvc.perform(post("/alunos")
                        .header("Authorization", tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].campo", hasItem("nome")));
    }

    @Test
    @DisplayName("Deve devolver 401 sem token")
    void acessarSemToken() throws Exception {
        mvc.perform(get("/alunos")).andExpect(status().isUnauthorized());
    }
}