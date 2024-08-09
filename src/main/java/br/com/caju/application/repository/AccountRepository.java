package br.com.caju.application.repository;

import br.com.caju.domain.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Optional<Account> findById(UUID id);
    Account update(Account account);
}
