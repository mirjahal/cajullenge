package br.com.caju.domain.predicate;

import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import jakarta.inject.Singleton;

import java.util.function.BiPredicate;

@Singleton
public class IsTheRequestedMccBelongsCategory implements BiPredicate<CategoryBalance, Transaction> {

    private final IsCategoryWithoutMcc isCategoryWithoutMcc;

    public IsTheRequestedMccBelongsCategory(IsCategoryWithoutMcc isCategoryWithoutMcc) {
        this.isCategoryWithoutMcc = isCategoryWithoutMcc;
    }

    @Override
    public boolean test(CategoryBalance categoryBalance, Transaction transaction) {
        if (isCategoryWithoutMcc.test(categoryBalance)) {
            return false;
        }

        return categoryBalance
                .getCategory()
                .getMerchantCategoryCodes()
                .contains(transaction.getMcc());
    }
}
