package br.com.caju.domain;

import br.com.caju.domain.exception.InsufficientBalanceException;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Serdeable
@Entity
@Table(name = "category_balances")
public class CategoryBalance {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @OneToOne
    private Category category;
    private BigDecimal balance;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void debitAmount(Transaction transaction) {
        if (this.getBalance().compareTo(ZERO) == 0 || this.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientBalanceException(transaction.getAmount());
        }

        BigDecimal newBalance = this.getBalance().subtract(transaction.getAmount());
        this.setBalance(newBalance);

        transaction.setCategoryBalance(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryBalance that = (CategoryBalance) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
