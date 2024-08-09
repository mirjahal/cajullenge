package br.com.caju.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(UUID accountId) {
        super("Account with id " + accountId + " does not exists");
    }
}
