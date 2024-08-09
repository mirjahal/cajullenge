package br.com.caju.application.usecase;

import br.com.caju.application.repository.AccountRepository;
import br.com.caju.application.repository.CategoryBalanceRepository;
import br.com.caju.application.repository.TransactionRepository;
import br.com.caju.domain.Account;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import br.com.caju.domain.TransactionStatus;
import br.com.caju.domain.exception.InsufficientBalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static br.com.caju.domain.TransactionStatus.*;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionAuthorizerTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CategoryBalanceRepository categoryBalanceRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private MccToCategoryBalanceMapper mccToCategoryBalanceMapper;
    @InjectMocks
    private TransactionAuthorizer transactionAuthorizer;

    @Test
    @DisplayName("Should return transaction status as APPROVED when transaction successfully processed")
    void shouldReturnStatusApproved() {
        var categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(TEN);

        var transaction = new Transaction();
        transaction.setAmount(TEN);

        when(accountRepository.findById(any())).thenReturn(Optional.of(new Account()));
        when(mccToCategoryBalanceMapper.map(any(), any())).thenReturn(categoryBalance);
        when(categoryBalanceRepository.update(any())).thenReturn(categoryBalance);
        when(transactionRepository.save(any())).thenReturn(transaction);

        TransactionStatus status = transactionAuthorizer.authorize(transaction, randomUUID());

        assertEquals(APPROVED, status);
    }

    @Test
    @DisplayName("Balance should be zero when transaction successfully processed")
    void balanceShouldBeZero() {
        var categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(TEN);

        var transaction = new Transaction();
        transaction.setAmount(TEN);

        when(accountRepository.findById(any())).thenReturn(Optional.of(new Account()));
        when(mccToCategoryBalanceMapper.map(any(), any())).thenReturn(categoryBalance);
        when(categoryBalanceRepository.update(any())).thenReturn(categoryBalance);
        when(transactionRepository.save(any())).thenReturn(transaction);

        transactionAuthorizer.authorize(transaction, randomUUID());

        assertEquals(ZERO, categoryBalance.getBalance());
    }

    @Test
    @DisplayName("Should return transaction status as REJECTED when account not found")
    void shouldReturnStatusRejected() {
        var transaction = new Transaction();
        transaction.setAmount(TEN);

        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        TransactionStatus status = transactionAuthorizer.authorize(transaction, randomUUID());

        assertEquals(REJECTED, status);
    }

    @Test
    @DisplayName("Should return transaction status as INSUFFICIENT_BALANCE when not found category with balance")
    void shouldReturnStatusInsufficientBalance() {
        var transaction = new Transaction();
        transaction.setAmount(TEN);

        when(accountRepository.findById(any())).thenReturn(Optional.of(new Account()));
        when(mccToCategoryBalanceMapper.map(any(), any())).thenThrow(InsufficientBalanceException.class);

        TransactionStatus status = transactionAuthorizer.authorize(transaction, randomUUID());

        assertEquals(INSUFFICIENT_BALANCE, status);
    }
}