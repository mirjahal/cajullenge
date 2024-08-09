package br.com.caju.infrastructure.db;

import br.com.caju.application.repository.CategoryBalanceRepository;
import br.com.caju.domain.CategoryBalance;
import br.com.caju.infrastructure.db.jpa.CategoryBalanceJpaRepository;
import jakarta.inject.Singleton;

@Singleton
public class CategoryBalanceDataRepository implements CategoryBalanceRepository {

    private final CategoryBalanceJpaRepository categoryBalanceJpaRepository;

    public CategoryBalanceDataRepository(CategoryBalanceJpaRepository categoryBalanceJpaRepository) {
        this.categoryBalanceJpaRepository = categoryBalanceJpaRepository;
    }

    @Override
    public CategoryBalance update(CategoryBalance categoryBalance) {
        return categoryBalanceJpaRepository.update(categoryBalance);
    }
}
