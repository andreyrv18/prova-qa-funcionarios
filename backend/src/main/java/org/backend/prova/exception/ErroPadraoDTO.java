package org.backend.prova.exception;

import java.time.Instant;

public record ErroPadraoDTO(
    Instant timestamp, Integer status, String error, String message, String path) {}
