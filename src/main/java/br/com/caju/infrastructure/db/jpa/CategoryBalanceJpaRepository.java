package br.com.caju.infrastructure.db.jpa;

import br.com.caju.domain.CategoryBalance;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Repository
public interface CategoryBalanceJpaRepository extends JpaRepository<CategoryBalance, UUID> {

}
