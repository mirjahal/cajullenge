package br.com.caju.application.usecase.selector;

import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.predicate.HasSufficientBalanceInCategory;
import br.com.caju.domain.predicate.IsTheRequestedMccBelongsCategory;
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
class MccMatchSelectorTest {

    @Mock
    private IsTheRequestedMccBelongsCategory isTheRequestedMccBelongsCategory;
    @Mock
    private HasSufficientBalanceInCategory hasSufficientBalanceInCategory;
    @InjectMocks
    private MccMatchSelector selector;

    @Test
    @DisplayName("Should set categoryBalanceSelected with current categoryBalanceToEvaluate")
    void selectCurrentCategoryBalanceToEvaluate() {
        when(isTheRequestedMccBelongsCategory.test(any(), any())).thenReturn(true);
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
        when(isTheRequestedMccBelongsCategory.test(any(), any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(false);

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());

        var context = new SelectorContext();
        context.setCategoryBalanceToEvaluate(categoryBalance);

        selector.select(context);

        assertNull(context.getCategoryBalanceSelected());
    }

    @Test
    @DisplayName("Should categoryBalanceSelected is null when mcc not belongs to current category")
    void categoryBalanceSelectedIsNullWhenMccNotBelongsToCategory() {
        when(isTheRequestedMccBelongsCategory.test(any(), any())).thenReturn(false);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);

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
        when(isTheRequestedMccBelongsCategory.test(any(), any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);

        var categoryBalance = new CategoryBalance();
        categoryBalance.setId(randomUUID());

        var context = new SelectorContext();
        context.setCategoryBalanceToEvaluate(categoryBalance);

        selector.select(context);

        assertFalse(context.isMccChanged());
    }
}