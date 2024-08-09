package br.com.caju.domain.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(BigDecimal amountToSubtract) {
        super("Unable to subtract amount " + amountToSubtract + ".");
    }
}
