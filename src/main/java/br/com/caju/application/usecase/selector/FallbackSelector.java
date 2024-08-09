package br.com.caju.application.usecase.selector;


import br.com.caju.domain.predicate.HasSufficientBalanceInCategory;
import br.com.caju.domain.predicate.IsCategoryWithoutMcc;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import io.micronaut.core.annotation.Order;
import jakarta.inject.Singleton;

@Singleton
@Order(3)
public class FallbackSelector extends CategoryBalanceSelector {

    private final IsCategoryWithoutMcc isCategoryWithoutMcc;
    private final HasSufficientBalanceInCategory hasSufficientBalanceInCategory;

    public FallbackSelector(IsCategoryWithoutMcc isCategoryWithoutMcc, HasSufficientBalanceInCategory hasSufficientBalanceInCategory) {
        this.isCategoryWithoutMcc = isCategoryWithoutMcc;
        this.hasSufficientBalanceInCategory = hasSufficientBalanceInCategory;
    }

    @Override
    public boolean select(SelectorContext context) {
        if (shouldSelect(context)) {
            context.setCategoryBalanceSelected(context.getCategoryBalanceToEvaluate());
            context.setMccChanged(false);
        }

        return selectNext(context);
    }

    private boolean shouldSelect(SelectorContext context) {
        Transaction transaction = context.getTransaction();
        CategoryBalance categoryBalanceToEvaluate = context.getCategoryBalanceToEvaluate();

        return context.getCategoryBalanceSelected() == null &&
                isCategoryWithoutMcc.test(categoryBalanceToEvaluate) &&
                hasSufficientBalanceInCategory.test(categoryBalanceToEvaluate, transaction);
    }
}
