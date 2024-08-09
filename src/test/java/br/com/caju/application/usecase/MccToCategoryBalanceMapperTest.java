package br.com.caju.application.usecase;

import br.com.caju.application.CategorySynonyms;
import br.com.caju.application.usecase.selector.CategoryBalanceSelector;
import br.com.caju.application.usecase.selector.CategoryBasedOnMerchantNameSelector;
import br.com.caju.application.usecase.selector.MccMatchSelector;
import br.com.caju.domain.Category;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;
import br.com.caju.domain.exception.InsufficientBalanceException;
import br.com.caju.domain.predicate.HasSufficientBalanceInCategory;
import br.com.caju.domain.predicate.IsCategoryWithMcc;
import br.com.caju.domain.predicate.IsTheRequestedMccBelongsCategory;
import br.com.caju.domain.predicate.IsTheRequestedMccNotBelongsCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MccToCategoryBalanceMapperTest {

    @Mock
    private IsTheRequestedMccBelongsCategory isTheRequestedMccBelongsCategory;
    @Mock
    private HasSufficientBalanceInCategory hasSufficientBalanceInCategory;
    @Mock
    private IsTheRequestedMccNotBelongsCategory isTheRequestedMccNotBelongsCategory;
    @Mock
    private IsCategoryWithMcc isCategoryWithMcc;
    @Mock
    private CategorySynonyms categorySynonyms;
    @InjectMocks
    private MccToCategoryBalanceMapper mapper;

    @BeforeEach
    void setUp() {
        var matchSelector = new MccMatchSelector(
                isTheRequestedMccBelongsCategory,
                hasSufficientBalanceInCategory
        );
        var categoryBasedOnMerchantNameSelector = new CategoryBasedOnMerchantNameSelector(
                hasSufficientBalanceInCategory,
                isTheRequestedMccNotBelongsCategory,
                isCategoryWithMcc,
                categorySynonyms
        );

        List<CategoryBalanceSelector> selectors = List.of(matchSelector, categoryBasedOnMerchantNameSelector);

        this.mapper = new MccToCategoryBalanceMapper(selectors);
    }

    @Test
    @DisplayName("Should return category balance without change mcc")
    void shouldReturnCategoryBalance() {
        when(isTheRequestedMccBelongsCategory.test(any(), any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);

        var categoryFood = new Category();
        categoryFood.setId("FOOD");
        categoryFood.setMerchantCategoryCodes(List.of(5411, 5412));

        var categoryBalanceFood = new CategoryBalance();
        categoryBalanceFood.setBalance(TEN);
        categoryBalanceFood.setCategory(categoryFood);

        List<CategoryBalance> categoryBalances = List.of(categoryBalanceFood);

        var transaction = new Transaction(randomUUID(), TEN, "ZE GROCERIES", 5411, now());

        CategoryBalance categoryBalanceSelected = mapper.map(categoryBalances, transaction);

        assertEquals(categoryBalanceFood, categoryBalanceSelected);
    }

    @Test
    @DisplayName("Should return category balance with change mcc")
    void shouldReturnCategoryBalanceWithChangeMcc() {
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(true);
        when(isTheRequestedMccNotBelongsCategory.test(any(), any())).thenReturn(true);
        when(isCategoryWithMcc.test(any())).thenReturn(true);
        when(categorySynonyms.getCategories()).thenReturn(Map.of("food", List.of("eats")));

        var categoryFood = new Category();
        categoryFood.setId("FOOD");
        categoryFood.setMerchantCategoryCodes(List.of(5411, 5412));

        var categoryBalanceFood = new CategoryBalance();
        categoryBalanceFood.setBalance(TEN);
        categoryBalanceFood.setCategory(categoryFood);

        List<CategoryBalance> categoryBalances = List.of(categoryBalanceFood);

        var transaction = new Transaction(randomUUID(), TEN, "ZE EATS", 6546, now());

        mapper.map(categoryBalances, transaction);

        assertEquals(5411, transaction.getMcc());
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when no category balance found")
    void shouldThrowInsufficientBalanceException() {
        when(isTheRequestedMccBelongsCategory.test(any(), any())).thenReturn(true);
        when(hasSufficientBalanceInCategory.test(any(), any())).thenReturn(false);

        var categoryFood = new Category();
        categoryFood.setId("FOOD");
        categoryFood.setMerchantCategoryCodes(List.of(5411, 5412));

        var categoryBalanceFood = new CategoryBalance();
        categoryBalanceFood.setBalance(ZERO);
        categoryBalanceFood.setCategory(categoryFood);

        List<CategoryBalance> categoryBalances = List.of(categoryBalanceFood);

        var transaction = new Transaction(randomUUID(), TEN, "ZE GROCERIES", 5411, now());

        assertThrows(InsufficientBalanceException.class, () -> mapper.map(categoryBalances, transaction));
    }
}