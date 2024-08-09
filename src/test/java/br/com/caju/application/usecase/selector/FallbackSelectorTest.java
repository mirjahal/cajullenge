package br.com.caju.application.usecase.selector;

import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.predicate.HasSufficientBalanceInCategory;
import br.com.caju.domain.predicate.IsCategoryWithoutMcc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FallbackSelectorTest {

    @Mock
    private IsCategoryWithoutMcc isCategoryWithoutMcc;
    @Mock
    private HasSufficientBalanceInCategory hasSufficientBalanceInCategory;
    @InjectMocks
    private FallbackSelector selector;

    @Test
    @DisplayName("Should set categoryBalanceSelected with current categoryBalanceToEvaluate")
    void selectCurrentCategoryBalanceToEvaluate() {
        when(isCategoryWithoutMcc.test(any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());

        var context = new SelectorContext();
        context.setCategoryBalanceToEvaluate(categoryBalance);

        selector.select(context);

        assertEquals(categoryBalance, context.getCategoryBalanceSelected());
    }

    @Test
    @DisplayName("Should categoryBalanceSelected is null when insufficient balance")
    void categoryBalanceSelectedIsNullWhenInsufficientBalance() {
        when(isCategoryWithoutMcc.test(any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(false);

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());

        var context = new SelectorContext();
        context.setCategoryBalanceToEvaluate(categoryBalance);

        selector.select(context);

        assertNull(context.getCategoryBalanceSelected());
    }

    @Test
    @DisplayName("Should categoryBalanceSelected is null when category with mcc list")
    void categoryBalanceSelectedIsNullWhenCategorywithMccList() {
        when(isCategoryWithoutMcc.test(any())).thenReturn(false);

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());

        var context = new SelectorContext();
        context.setCategoryBalanceToEvaluate(categoryBalance);

        selector.select(context);

        assertNull(context.getCategoryBalanceSelected());
    }

    @Test
    @DisplayName("Should set mccChanged to false")
    void setMccChangedToFalse() {
        when(isCategoryWithoutMcc.test(any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());

        var context = new SelectorContext();
        context.setCategoryBalanceToEvaluate(categoryBalance);
        context.setMccChanged(true);

        selector.select(context);

        assertFalse(context.isMccChanged());
    }
}