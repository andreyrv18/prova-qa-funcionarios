package org.backend.prova.exception;

public class NoHandlerFoundException extends RuntimeException {
  public NoHandlerFoundException(String mensagem) {
    super(mensagem);
  }
}
