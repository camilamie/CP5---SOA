package br.com.fiap.autoescola.controller;

import br.com.fiap.autoescola.ApiIntegrationTest;
import br.com.fiap.autoescola.domain.aluno.Aluno;
import br.com.fiap.autoescola.domain.aluno.AlunoRepository;
import br.com.fiap.autoescola.domain.aluno.DadosCadastroAluno;
import br.com.fiap.autoescola.domain.endereco.DadosEndereco;
import br.com.fiap.autoescola.domain.instrutor.DadosCadastroInstrutor;
import br.com.fiap.autoescola.domain.instrutor.Especialidade;
import br.com.fiap.autoescola.domain.instrutor.Instrutor;
import br.com.fiap.autoescola.domain.instrutor.InstrutorRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InstrucaoControllerTest extends ApiIntegrationTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    private Aluno aluno;
    private Instrutor instrutor;
    private String token;

    // Segunda-feira daqui a mais de uma semana: respeita as regras de antecedência
    private final LocalDateTime proximaSegunda10h = LocalDate.now().plusWeeks(1)
            .with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);

    @BeforeEach
    void preparar() throws Exception {
        var endereco = new DadosEndereco("Rua A", "10", null, "Centro", "São Paulo", "SP", "01001000");
        aluno = alunoRepository.save(new Aluno(new DadosCadastroAluno(
                "Aluno Teste", "aluno@teste.com", "11911112222", "11122233344", endereco)));
        instrutor = instrutorRepository.save(new Instrutor(new DadosCadastroInstrutor(
                "Instrutor Teste", "instrutor@teste.com", "11933334444", "99988877766", Especialidade.CARROS, endereco)));
        token = tokenAdmin();
    }

    private String jsonAgendamento(LocalDateTime data) {
        return """
                {"idAluno": %d, "idInstrutor": %d, "data": "%s"}
                """.formatted(aluno.getId(), instrutor.getId(), data);
    }

    private Long agendar(LocalDateTime data) throws Exception {
        var resposta = mvc.perform(post("/instrucoes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAgendamento(data)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(resposta, "$.id")).longValue();
    }

    @Test
    @DisplayName("Deve agendar instrução em horário válido e devolver 201")
    void agendarComSucesso() throws Exception {
        mvc.perform(post("/instrucoes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAgendamento(proximaSegunda10h)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeAluno").value("Aluno Teste"))
                .andExpect(jsonPath("$.nomeInstrutor").value("Instrutor Teste"))
                .andExpect(jsonPath("$.motivoCancelamento").value(nullValue()));
    }

    @Test
    @DisplayName("Deve escolher um instrutor livre quando idInstrutor não for informado")
    void agendarSemInstrutor() throws Exception {
        mvc.perform(post("/instrucoes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idAluno": %d, "data": "%s"}
                                """.formatted(aluno.getId(), proximaSegunda10h)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInstrutor").exists());
    }

    @Test
    @DisplayName("Não deve agendar instrução no domingo")
    void agendarNoDomingo() throws Exception {
        var domingo = proximaSegunda10h.minusDays(1);

        mvc.perform(post("/instrucoes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAgendamento(domingo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    @DisplayName("Não deve agendar duas instruções com o mesmo instrutor no mesmo horário")
    void instrutorOcupado() throws Exception {
        agendar(proximaSegunda10h);

        mvc.perform(post("/instrucoes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAgendamento(proximaSegunda10h.plusMinutes(30))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("O instrutor já possui outra instrução agendada nesse horário"));
    }

    @Test
    @DisplayName("Deve cancelar instrução informando o motivo, e não permitir cancelar de novo")
    void cancelar() throws Exception {
        var idInstrucao = agendar(proximaSegunda10h);
        var json = """
                {"idInstrucao": %d, "motivo": "ALUNO_DESISTIU"}
                """.formatted(idInstrucao);

        mvc.perform(delete("/instrucoes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        mvc.perform(delete("/instrucoes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Essa instrução já foi cancelada"));
    }
}