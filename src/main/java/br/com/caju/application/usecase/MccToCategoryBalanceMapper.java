package br.com.caju.application.usecase;

import br.com.caju.application.usecase.selector.CategoryBalanceSelector;
import br.com.caju.application.usecase.selector.SelectorContext;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import br.com.caju.domain.exception.InsufficientBalanceException;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class MccToCategoryBalanceMapper {

    private final List<CategoryBalanceSelector> categoryBalanceSelectors;

    public MccToCategoryBalanceMapper(List<CategoryBalanceSelector> categoryBalanceSelectors) {
        this.categoryBalanceSelectors = categoryBalanceSelectors;
    }

    public CategoryBalance map(List<CategoryBalance> categoryBalances, Transaction transaction) {
        CategoryBalanceSelector categoryBalanceSelector = CategoryBalanceSelector.link(categoryBalanceSelectors);

        SelectorContext context = new SelectorContext();
        context.setTransaction(transaction);

        categoryBalances
                .forEach(categoryBalance -> {
                    context.setCategoryBalanceToEvaluate(categoryBalance);
                    context.setTransaction(transaction);

                    categoryBalanceSelector.select(context);
                });

        if (context.getCategoryBalanceSelected() == null) {
            throw new InsufficientBalanceException(transaction.getAmount());
        }

        if (context.isMccChanged()) {
            transaction.setMcc(context.getCategoryBalanceSelected().getCategory().getMerchantCategoryCodes().getFirst());
        }

        return context.getCategoryBalanceSelected();
    }
}
