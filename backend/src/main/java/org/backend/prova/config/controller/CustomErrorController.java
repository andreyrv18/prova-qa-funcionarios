package org.backend.prova.config.controller; // Ajuste o pacote conforme seu projeto

import jakarta.servlet.http.HttpServletRequest;
import org.backend.prova.exception.ErroPadraoDTO;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;

@RestController
public class CustomErrorController implements ErrorController {

  @RequestMapping("/error")
  public ResponseEntity<ErroPadraoDTO> handleError(HttpServletRequest request) {
    // 1. Recupera o status do erro (404, 500, etc) injetado pelo Spring
    Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
    if (statusCode == null) {
      statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    // 2. Recupera a mensagem de erro padrão do Spring
    String message = (String) request.getAttribute("jakarta.servlet.error.message");
    if (message == null || message.isEmpty() || message.equals("No message available")) {
      message = "Ocorreu um erro ao processar sua requisição.";
    }

    // 3. Recupera a URI original que causou o erro
    String path = (String) request.getAttribute("jakarta.servlet.error.request_uri");
    if (path == null) {
      path = request.getRequestURI();
    }

    // 4. Define o título do erro com base no status (Java 21 Switch Expression!)
    HttpStatus status = HttpStatus.resolve(statusCode);
    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    String errorTitle =
        switch (status) {
          case NOT_FOUND -> "Rota não encontrada";
          case METHOD_NOT_ALLOWED -> "Método HTTP não permitido";
          case BAD_REQUEST -> "Requisição inválida";
          case UNPROCESSABLE_CONTENT -> "Erro de regra de negócio";
          default -> "Erro interno do servidor";
        };

    // 5. Monta e retorna o seu DTO padronizado
    ErroPadraoDTO erro =
        new ErroPadraoDTO(Instant.now(), status.value(), errorTitle, message, path);

    return ResponseEntity.status(status).body(erro);
  }
}
