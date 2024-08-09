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

class IsCategoryWithoutMccTest {

    private IsCategoryWithoutMcc predicate;

    @BeforeEach
    void setUp() {
        this.predicate = new IsCategoryWithoutMcc();
    }

    @Test
    @DisplayName("Should return true when mcc list is null")
    void returnTrueWhenMccListIsNull() {
        Category category = new Category();
        category.setMerchantCategoryCodes(null);

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        assertTrue(predicate.test(categoryBalance));
    }

    @Test
    @DisplayName("Should return true when mcc list is empty")
    void returnTrueWhenMccListIsEmpty() {
        Category category = new Category();
        category.setMerchantCategoryCodes(emptyList());

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        assertTrue(predicate.test(categoryBalance));
    }

    @Test
    @DisplayName("Should return false when mcc list is not empty")
    void returnFalseWhenMccListIsNotEmpty() {
        Category category = new Category();
        category.setMerchantCategoryCodes(List.of(5411, 5412));

        CategoryBalance categoryBalance = new CategoryBalance();
        categoryBalance.setCategory(category);

        assertFalse(predicate.test(categoryBalance));
    }
}