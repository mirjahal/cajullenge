package br.com.caju.domain;

import br.com.caju.domain.exception.InsufficientBalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryBalanceTest {

    @Test
    @DisplayName("Should debit amount successfully when balance greater than amount")
    void debitAmountWhenSufficienBalance() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(500));

        categoryBalance.debitAmount(transaction);

        assertEquals(BigDecimal.valueOf(400), categoryBalance.getBalance());
    }

    @Test
    @DisplayName("Should debit amount successfully when balance equals to amount")
    void debitAmountWhenBalanceEqualsToAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(500));

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(500));

        categoryBalance.debitAmount(transaction);

        assertEquals(BigDecimal.ZERO, categoryBalance.getBalance());
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when balance less than amount")
    void throwsExceptionWhenBalanceLessThanAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(600));

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(500));

        assertThrows(InsufficientBalanceException.class, () -> categoryBalance.debitAmount(transaction));
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException always when balance equals to zero")
    void throwsExceptionWhenBalanceEqualsToZero() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(0));

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(0));

        assertThrows(InsufficientBalanceException.class, () -> categoryBalance.debitAmount(transaction));
    }
}