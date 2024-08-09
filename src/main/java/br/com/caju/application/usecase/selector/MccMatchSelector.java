package br.com.caju.application.usecase.selector;

import br.com.caju.domain.predicate.HasSufficientBalanceInCategory;
import br.com.caju.domain.predicate.IsTheRequestedMccBelongsCategory;
import io.micronaut.core.annotation.Order;
import jakarta.inject.Singleton;

@Singleton
@Order(1)
public class MccMatchSelector extends CategoryBalanceSelector {

    private final IsTheRequestedMccBelongsCategory isTheRequestedMccBelongsCategory;
    private final HasSufficientBalanceInCategory hasSufficientBalanceInCategory;

    public MccMatchSelector(
            IsTheRequestedMccBelongsCategory isTheRequestedMccBelongsCategory,
            HasSufficientBalanceInCategory hasSufficientBalanceInCategory
    ) {
        this.isTheRequestedMccBelongsCategory = isTheRequestedMccBelongsCategory;
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
        var categoryBalance = context.getCategoryBalanceToEvaluate();
        var transaction = context.getTransaction();

        boolean isTheRequestedMccBelongsCategory = this.isTheRequestedMccBelongsCategory.test(categoryBalance, transaction);
        boolean hasSufficientBalanceInCategory = this.hasSufficientBalanceInCategory.test(categoryBalance, transaction);

        return isTheRequestedMccBelongsCategory && hasSufficientBalanceInCategory;
    }
}
