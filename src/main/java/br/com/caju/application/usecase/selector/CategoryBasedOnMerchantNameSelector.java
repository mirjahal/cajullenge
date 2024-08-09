package br.com.caju.application.usecase.selector;

import br.com.caju.application.CategorySynonyms;
import br.com.caju.domain.predicate.HasSufficientBalanceInCategory;
import br.com.caju.domain.predicate.IsCategoryWithMcc;
import br.com.caju.domain.predicate.IsTheRequestedMccNotBelongsCategory;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import io.micronaut.core.annotation.Order;
import jakarta.inject.Singleton;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Singleton
@Order(2)
public class CategoryBasedOnMerchantNameSelector extends CategoryBalanceSelector {

    private final HasSufficientBalanceInCategory hasSufficientBalanceInCategory;
    private final IsTheRequestedMccNotBelongsCategory isTheRequestedMccNotBelongsCategory;
    private final IsCategoryWithMcc isCategoryWithMcc;
    private final CategorySynonyms categorySynonyms;

    public CategoryBasedOnMerchantNameSelector(
            HasSufficientBalanceInCategory hasSufficientBalanceInCategory,
            IsTheRequestedMccNotBelongsCategory isTheRequestedMccNotBelongsCategory,
            IsCategoryWithMcc isCategoryWithMcc,
            CategorySynonyms categorySynonyms) {

        this.hasSufficientBalanceInCategory = hasSufficientBalanceInCategory;
        this.isTheRequestedMccNotBelongsCategory = isTheRequestedMccNotBelongsCategory;
        this.isCategoryWithMcc = isCategoryWithMcc;
        this.categorySynonyms = categorySynonyms;
    }

    @Override
    public boolean select(SelectorContext context) {
        if (shouldSelect(context)) {
            context.setCategoryBalanceSelected(context.getCategoryBalanceToEvaluate());
            context.setMccChanged(true);
        }

        return selectNext(context);
    }

    private boolean shouldSelect(SelectorContext context) {
        Transaction transaction = context.getTransaction();
        CategoryBalance categoryBalanceToEvaluate = context.getCategoryBalanceToEvaluate();
        String categoryFromMerchantName = this.extractCategoryFromMerchantName(transaction);

        return context.getCategoryBalanceSelected() == null &&
                categoryBalanceToEvaluate.getCategory().getId().equalsIgnoreCase(categoryFromMerchantName) &&
                isCategoryWithMcc.test(categoryBalanceToEvaluate) &&
                isTheRequestedMccNotBelongsCategory.test(categoryBalanceToEvaluate, transaction) &&
                hasSufficientBalanceInCategory.test(categoryBalanceToEvaluate, transaction);
    }

    private String extractCategoryFromMerchantName(Transaction transaction) {
        List<String> merchantName = Arrays.stream(transaction.getMerchant().toLowerCase().split(" ")).toList();

        for (Map.Entry<String, List<String>> entry : categorySynonyms.getCategories().entrySet()) {
            if (CollectionUtils.containsAny(entry.getValue(), merchantName)) {
                return entry.getKey();
            }
        }

        return null;
    }
}
