package br.com.caju.infrastructure.db;

import br.com.caju.application.repository.TransactionRepository;
import br.com.caju.domain.Transaction;
import br.com.caju.infrastructure.db.jpa.TransactionJpaRepository;
import jakarta.inject.Singleton;

@Singleton
public class TransactionDataRepository implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;

    public TransactionDataRepository(TransactionJpaRepository transactionJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
    }

    @Override
    public Transaction save(Transaction account) {
        return transactionJpaRepository.save(account);
    }
}
