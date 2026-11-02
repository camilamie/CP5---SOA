package br.com.fiap.autoescola;

import br.com.fiap.autoescola.domain.usuario.Perfil;
import br.com.fiap.autoescola.domain.usuario.Usuario;
import br.com.fiap.autoescola.domain.usuario.UsuarioRepository;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class ApiIntegrationTest {

    @Autowired protected MockMvc mvc;
    @Autowired protected UsuarioRepository usuarioRepository;
    @Autowired protected PasswordEncoder passwordEncoder;

    protected String tokenAdmin() throws Exception {
        return login("admin", "admin123");
    }

    protected String tokenUser() throws Exception {
        usuarioRepository.save(new Usuario("usuario.teste", passwordEncoder.encode("senha123"), Perfil.USER));
        return login("usuario.teste", "senha123");
    }

    protected String login(String login, String senha) throws Exception {
        var resposta = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"login": "%s", "senha": "%s"}
                                """.formatted(login, senha)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(resposta, "$.token");
    }

    protected static String jsonEndereco() {
        return """
                {"logradouro": "Av. Paulista", "numero": "1106", "complemento": "",
                 "bairro": "Bela Vista", "cidade": "São Paulo", "uf": "sp", "cep": "01311000"}
                """;
    }
}