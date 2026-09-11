package br.com.fiap.autoescola.infra.exception;

import br.com.fiap.autoescola.domain.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DadosErro> tratarErro404() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosErro("Registro não encontrado"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DadosErro> tratarJsonInvalido() {
        return ResponseEntity.badRequest().body(new DadosErro(
                "JSON inválido. Verifique a sintaxe, os valores permitidos dos campos (ex.: especialidade, perfil, motivo) " +
                        "e o formato da data (ex.: 2026-09-20T10:00:00)"));
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosErro> tratarRegraDeNegocio(ValidacaoException ex) {
        return ResponseEntity.badRequest().body(new DadosErro(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<DadosErro> tratarDuplicidade() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosErro(
                "Já existe um registro com esses dados (e-mail, CPF, CNH ou login duplicado)"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DadosErro> tratarCredenciaisInvalidas() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DadosErro("Login ou senha inválidos"));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<DadosErro> tratarUsuarioInativo() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DadosErro("Usuário inativo"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosErro> tratarAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DadosErro("Acesso negado"));
    }

    public record DadosErro(String erro) {
    }

    public record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}