package br.com.caju.domain.predicate;

import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import jakarta.inject.Singleton;

import java.util.function.BiPredicate;

import static java.math.BigDecimal.ZERO;

@Singleton
public class HasSufficientBalanceInCategory implements BiPredicate<CategoryBalance, Transaction> {

    @Override
    public boolean test(CategoryBalance categoryBalance, Transaction transaction) {
        if (categoryBalance.getBalance().compareTo(ZERO) == 0) {
            return false;
        }

        return categoryBalance.getBalance().compareTo(transaction.getAmount()) >= 0;
    }
}
