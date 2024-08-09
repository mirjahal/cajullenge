package br.com.caju.domain.predicate;

import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HasSufficientBalanceInCategoryTest {

    private HasSufficientBalanceInCategory predicate;

    @BeforeEach
    void setUp() {
        this.predicate = new HasSufficientBalanceInCategory();
    }

    @Test
    @DisplayName("Should return true when balance greater than amount")
    void returnTrueWhenBalanceGreaterThanAmount() {
        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(600));

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(500));

        assertTrue(predicate.test(categoryBalance, transaction));
    }

    @Test
    @DisplayName("Should return true when balance equals to amount")
    void returnTrueWhenBalanceEqualsToAmount() {
        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(500));

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(500));

        assertTrue(predicate.test(categoryBalance, transaction));
    }

    @Test
    @DisplayName("Should return true when balance less than amount")
    void returnFalseWhenBalanceLessThanAmount() {
        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(400));

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(500));

        assertFalse(predicate.test(categoryBalance, transaction));
    }

    @Test
    @DisplayName("Should return false always when balance equals to zero")
    void returnFalseWhenBalanceEqualsToZero() {
        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setBalance(BigDecimal.valueOf(0));

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(1));

        assertFalse(predicate.test(categoryBalance, transaction));
    }
}