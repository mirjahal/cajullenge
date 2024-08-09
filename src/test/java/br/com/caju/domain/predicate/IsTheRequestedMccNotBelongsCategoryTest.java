package br.com.caju.domain.predicate;

import br.com.caju.domain.Category;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.math.BigDecimal.TEN;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsTheRequestedMccNotBelongsCategoryTest {

    @Mock
    private IsCategoryWithoutMcc isCategoryWithoutMcc;

    @InjectMocks
    private IsTheRequestedMccNotBelongsCategory predicate;

    @Test
    @DisplayName("Should return true when mcc is not present in list")
    void returnTrueWhenMccNotPresentInList() {
        when(isCategoryWithoutMcc.test(any())).thenReturn(false);

        var category = new Category();
        category.setMerchantCategoryCodes(List.of(5411, 5412));

        var categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        var transaction = new Transaction(randomUUID(), TEN, "FOOD MEAL", 5812, now());

        assertTrue(predicate.test(categoryBalance, transaction));
    }

    @Test
    @DisplayName("Should return false when mcc is present in list")
    void returnFalseWhenMccPresentInList() {
        when(isCategoryWithoutMcc.test(any())).thenReturn(false);

        var category = new Category();
        category.setMerchantCategoryCodes(List.of(5411, 5412));

        var categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        var transaction = new Transaction(randomUUID(), TEN, "FOOD EATS", 5411, now());

        assertFalse(predicate.test(categoryBalance, transaction));
    }

    @Test
    @DisplayName("Should return true when category without mcc")
    void returnTrueWhenCategoryWithoutMcc() {
        when(isCategoryWithoutMcc.test(any())).thenReturn(true);

        var category = new Category();
        category.setMerchantCategoryCodes(null);

        var categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        var transaction = new Transaction(randomUUID(), TEN, "FOOD MEAL", 5812, now());

        assertTrue(predicate.test(categoryBalance, transaction));
    }
}