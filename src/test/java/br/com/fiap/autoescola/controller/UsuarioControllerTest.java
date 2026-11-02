package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.ApiIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTest extends ApiIntegrationTest {

    @Test
    @DisplayName("ADMIN deve conseguir cadastrar usuário (perfil USER por padrão)")
    void adminCadastraUsuario() throws Exception {
        mvc.perform(post("/usuarios")
                        .header("Authorization", tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login": "secretaria", "senha": "123456"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("secretaria"))
                .andExpect(jsonPath("$.perfil").value("USER"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    @DisplayName("Usuário com perfil USER deve receber 403 ao acessar /usuarios")
    void userNaoAcessaUsuarios() throws Exception {
        mvc.perform(get("/usuarios").header("Authorization", tokenUser()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve devolver 400 quando a senha tiver menos de 6 caracteres")
    void senhaCurta() throws Exception {
        mvc.perform(post("/usuarios")
                        .header("Authorization", tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login": "fulano", "senha": "123"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].campo").value("senha"));
    }

    @Test
    @DisplayName("Login com senha errada deve devolver 401")
    void loginInvalido() throws Exception {
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login": "admin", "senha": "errada"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}