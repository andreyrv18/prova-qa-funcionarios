package org.backend.prova.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // id que não existe no banco
  @ExceptionHandler(RecursoNaoEncontradoException.class)
  public ResponseEntity<ErroPadraoDTO> lidarComRecursoNaoEncontrado(
      RecursoNaoEncontradoException e, HttpServletRequest request) {

    HttpStatus codigoStatus = HttpStatus.NOT_FOUND;

    ErroPadraoDTO erro =
        new ErroPadraoDTO(
            Instant.now(),
            codigoStatus.value(),
            "Recurso não encontrado",
            e.getMessage(),
            request.getRequestURI());

    return ResponseEntity.status(codigoStatus).body(erro);
  }

  // Regra de negógio Violada
  @ExceptionHandler(RegraDeNegocioException.class)
  public ResponseEntity<ErroPadraoDTO> lidarComRegraDeNegocio(
      RegraDeNegocioException e, HttpServletRequest request) {

    HttpStatus codigoStatus = HttpStatus.UNPROCESSABLE_CONTENT;

    ErroPadraoDTO erro =
        new ErroPadraoDTO(
            Instant.now(),
            codigoStatus.value(),
            "Erro de regra de negócio",
            e.getMessage(),
            request.getRequestURI());

    return ResponseEntity.status(codigoStatus).body(erro);
  }

  // Rotas Digitadas errado
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErroPadraoDTO> handleException(
      NoResourceFoundException exception, HttpServletRequest request)
      throws RecursoNaoEncontradoException {
    HttpStatus status = HttpStatus.NOT_FOUND;
    String rota = request.getRequestURI();

    ErroPadraoDTO error =
        new ErroPadraoDTO(
            Instant.now(), status.value(), "rota_digitada", "Ops! rota não encontrado", rota);

    log.warn("Usuário tentou acessar rota inexistente: {}", exception.getResourcePath());

    return ResponseEntity.status(status).body(error);
  }

  //  Usado para Campos Obrigatórios no Json
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErroPadraoDTO> lidarComDadosJsonError(
      MethodArgumentNotValidException e, HttpServletRequest request) {

    HttpStatus codigoStatus = HttpStatus.BAD_REQUEST;

    String mensagemErro =
        e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .findFirst()
            .orElse("Erro de validação nos dados enviados.");

    ErroPadraoDTO error =
        new ErroPadraoDTO(
            Instant.now(),
            codigoStatus.value(), // 400
            "Erro de validação",
            mensagemErro,
            request.getRequestURI());

    return ResponseEntity.status(codigoStatus).body(error);
  }

  @ExceptionHandler(RegistroDuplicadoException.class)
  public ResponseEntity<ErroPadraoDTO> lidaComRegistroDuplicado(
      RegistroDuplicadoException e, HttpServletRequest request) {

    HttpStatus codigoStatus = HttpStatus.CONFLICT;
    ErroPadraoDTO erro =
        new ErroPadraoDTO(
            Instant.now(),
            codigoStatus.value(),
            "Erro de duplicidade",
            e.getMessage(),
            request.getRequestURI());

    return ResponseEntity.status(codigoStatus).body(erro);
  }
}
