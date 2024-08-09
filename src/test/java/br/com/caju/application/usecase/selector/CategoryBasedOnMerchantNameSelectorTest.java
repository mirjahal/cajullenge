package br.com.caju.application.usecase.selector;

import br.com.caju.application.CategorySynonyms;
import br.com.caju.domain.Category;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import br.com.caju.domain.predicate.HasSufficientBalanceInCategory;
import br.com.caju.domain.predicate.IsCategoryWithMcc;
import br.com.caju.domain.predicate.IsTheRequestedMccNotBelongsCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.TEN;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryBasedOnMerchantNameSelectorTest {

    @Mock
    private HasSufficientBalanceInCategory hasSufficientBalanceInCategory;
    @Mock
    private IsTheRequestedMccNotBelongsCategory isTheRequestedMccNotBelongsCategory;
    @Mock
    private IsCategoryWithMcc isCategoryWithMcc;
    @Mock
    private CategorySynonyms categorySynonyms;
    @InjectMocks
    private CategoryBasedOnMerchantNameSelector selector;

    @Test
    @DisplayName("Should set categoryBalanceSelected with categoryBalanceToEvaluate when category based on merchant name")
    void setCategoryBalanceSelectedWhenBasedOnMerchantName() {
        when(categorySynonyms.getCategories()).thenReturn(Map.of("meal", List.of("restaurant"), "food", List.of("eats")));
        when(isCategoryWithMcc.test(any())).thenReturn(true);
        when(isTheRequestedMccNotBelongsCategory.test(any(), any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);

        var category = new Category();
        category.setId("MEAL");

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());
        categoryBalance.setCategory(category);

        var transaction = new Transaction(randomUUID(), TEN, "ZE RESTAURANT", 0000, now());

        var context = new SelectorContext();
        context.setCategoryBalanceSelected(null);
        context.setCategoryBalanceToEvaluate(categoryBalance);
        context.setTransaction(transaction);

        selector.select(context);

        assertEquals(categoryBalance, context.getCategoryBalanceSelected());
    }

    @Test
    @DisplayName("Should set mccChanged to true when category based on merchant name")
    void setSetMccChangedWhenBasedOnMerchantName() {
        when(categorySynonyms.getCategories()).thenReturn(Map.of("meal", List.of("restaurant"), "food", List.of("eats")));
        when(isCategoryWithMcc.test(any())).thenReturn(true);
        when(isTheRequestedMccNotBelongsCategory.test(any(), any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);

        var category = new Category();
        category.setId("MEAL");

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());
        categoryBalance.setCategory(category);

        var transaction = new Transaction(randomUUID(), TEN, "ZE RESTAURANT", 0000, now());

        var context = new SelectorContext();
        context.setCategoryBalanceSelected(null);
        context.setCategoryBalanceToEvaluate(categoryBalance);
        context.setTransaction(transaction);

        selector.select(context);

        assertTrue(context.isMccChanged());
    }

    @Test
    @DisplayName("Should categoryBalanceSelected is null when category is not equals to extracted on merchant name")
    void categoryBalanceSelectedIsNullWhenCategoryIsNotMacth() {
        when(categorySynonyms.getCategories()).thenReturn(Map.of("meal", List.of("restaurant"), "food", List.of("eats")));

        var category = new Category();
        category.setId("FOOD");

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());
        categoryBalance.setCategory(category);

        var transaction = new Transaction(randomUUID(), TEN, "ZE RESTAURANT", 0000, now());

        var context = new SelectorContext();
        context.setCategoryBalanceSelected(null);
        context.setCategoryBalanceToEvaluate(categoryBalance);
        context.setTransaction(transaction);

        selector.select(context);

        assertNull(context.getCategoryBalanceSelected());
    }

    @Test
    @DisplayName("Should not to set mccChanged to true when categoryBalanceSelected already defined previously")
    void mccChangedIsFalse() {
        when(categorySynonyms.getCategories()).thenReturn(Map.of("meal", List.of("restaurant"), "food", List.of("eats")));

        var category = new Category();
        category.setId("FOOD");

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());
        categoryBalance.setCategory(category);

        var transaction = new Transaction(randomUUID(), TEN, "ZE RESTAURANT", 0000, now());

        var context = new SelectorContext();
        context.setCategoryBalanceSelected(categoryBalance);
        context.setCategoryBalanceToEvaluate(categoryBalance);
        context.setTransaction(transaction);

        selector.select(context);

        assertFalse(context.isMccChanged());
    }
}