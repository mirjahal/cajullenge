package br.com.caju.domain.predicate;

import br.com.caju.domain.CategoryBalance;
import jakarta.inject.Singleton;

import java.util.function.Predicate;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Singleton
public class IsCategoryWithMcc implements Predicate<CategoryBalance> {

    @Override
    public boolean test(CategoryBalance categoryBalance) {
        return isNotEmpty(categoryBalance.getCategory().getMerchantCategoryCodes());
    }
}
