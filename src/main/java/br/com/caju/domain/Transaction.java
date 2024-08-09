package br.com.caju.domain;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Serdeable
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private UUID id;
    @OneToOne
    private CategoryBalance categoryBalance;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String merchant;
    private int mcc;

    public Transaction(UUID id, BigDecimal amount, String merchant, int mcc, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.mcc = mcc;
        this.createdAt = createdAt;
    }

    public Transaction() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMcc() {
        return mcc;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCategoryBalance(CategoryBalance categoryBalance) {
        this.categoryBalance = categoryBalance;
    }

    public CategoryBalance getCategoryBalance() {
        return categoryBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transaction that = (Transaction) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
