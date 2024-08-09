package br.com.caju.application.usecase;

import br.com.caju.application.repository.AccountRepository;
import br.com.caju.application.repository.CategoryBalanceRepository;
import br.com.caju.application.repository.TransactionRepository;
import br.com.caju.domain.Account;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import br.com.caju.domain.TransactionStatus;
import br.com.caju.domain.exception.AccountNotFoundException;
import br.com.caju.domain.exception.InsufficientBalanceException;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static br.com.caju.domain.TransactionStatus.*;

@Singleton
public class TransactionAuthorizer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionAuthorizer.class);
    private final AccountRepository accountRepository;
    private final CategoryBalanceRepository categoryBalanceRepository;
    private final TransactionRepository transactionRepository;
    private final MccToCategoryBalanceMapper mccToCategoryBalanceMapper;

    public TransactionAuthorizer(
            AccountRepository accountRepository,
            CategoryBalanceRepository categoryBalanceRepository,
            TransactionRepository transactionRepository,
            MccToCategoryBalanceMapper mccToCategoryBalanceMapper) {

        this.accountRepository =accountRepository;
        this.categoryBalanceRepository = categoryBalanceRepository;
        this.transactionRepository = transactionRepository;
        this.mccToCategoryBalanceMapper = mccToCategoryBalanceMapper;
    }

    @Transactional
    public TransactionStatus authorize(Transaction transaction, UUID accountId) {
        try {
            Account account = findAccountById(accountId);

            CategoryBalance categoryBalanceSelected = mccToCategoryBalanceMapper.map(account.getCategoryBalances(), transaction);
            categoryBalanceSelected.debitAmount(transaction);

            categoryBalanceRepository.update(categoryBalanceSelected);
            transactionRepository.save(transaction);
        } catch (InsufficientBalanceException insufficientBalanceException) {
            logger.info(insufficientBalanceException.getMessage(), insufficientBalanceException);
            return INSUFFICIENT_BALANCE;
        } catch (Exception exception) {
            logger.info(exception.getMessage(), exception);
            return REJECTED;
        }

        return APPROVED;
    }

    private Account findAccountById(UUID accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        return account.get();
    }
}
