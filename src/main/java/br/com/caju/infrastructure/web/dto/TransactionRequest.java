package br.com.caju.infrastructure.web.dto;

import br.com.caju.domain.Transaction;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Serdeable
public record TransactionRequest(
    @NotNull UUID id,
    @NotNull UUID accountId,
    @NotNull BigDecimal amount,
    @NotBlank String merchant,
    @Positive int mcc
) {
    public Transaction toTransaction() {
        return new Transaction(
                this.id(),
                this.amount(),
                this.merchant(),
                this.mcc(),
                LocalDateTime.now()
        );
    }
}