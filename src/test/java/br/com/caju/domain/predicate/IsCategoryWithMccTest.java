package br.com.caju.domain.predicate;

import br.com.caju.domain.Category;
import br.com.caju.domain.CategoryBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsCategoryWithMccTest {

    private IsCategoryWithMcc predicate;

    @BeforeEach
    void setUp() {
        this.predicate = new IsCategoryWithMcc();
    }

    @Test
    @DisplayName("Should return true whe mcc list in category is not empty")
    void returnTrueWhenMccListIsNotEmpty() {
        Category category = new Category();
        category.setMerchantCategoryCodes(List.of(5411, 5412));

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        assertTrue(predicate.test(categoryBalance));
    }

    @Test
    @DisplayName("Should return false whe mcc list in category is null")
    void returnFalseWhenMccListIsNull() {
        Category category = new Category();
        category.setMerchantCategoryCodes(null);

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        assertFalse(predicate.test(categoryBalance));
    }

    @Test
    @DisplayName("Should return false whe mcc list in category is empty")
    void returnFalseWhenMccListIsEmpty() {
        Category category = new Category();
        category.setMerchantCategoryCodes(emptyList());

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        assertFalse(predicate.test(categoryBalance));
    }
}