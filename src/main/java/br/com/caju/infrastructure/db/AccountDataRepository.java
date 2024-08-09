package br.com.caju.infrastructure.db;

import br.com.caju.application.repository.AccountRepository;
import br.com.caju.domain.Account;
import br.com.caju.infrastructure.db.jpa.AccountJpaRepository;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class AccountDataRepository implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    public AccountDataRepository(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountJpaRepository.findById(id);
    }

    @Override
    public Account update(Account account) {
        return accountJpaRepository.update(account);
    }
}
