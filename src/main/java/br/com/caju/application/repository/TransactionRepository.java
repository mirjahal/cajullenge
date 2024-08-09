package br.com.caju.application.repository;

import br.com.caju.domain.Transaction;

public interface TransactionRepository {

    Transaction save(Transaction transaction);
}
