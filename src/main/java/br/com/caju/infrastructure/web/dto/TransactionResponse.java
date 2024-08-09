package br.com.caju.infrastructure.web.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TransactionResponse(
    String code
) { }
