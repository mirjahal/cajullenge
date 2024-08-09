package br.com.caju.domain;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Serdeable
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private UUID id;

    private String description;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CategoryBalance> categoryBalances;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCategoryBalances(List<CategoryBalance> categoryBalances) {
        this.categoryBalances = categoryBalances;
    }

    public List<CategoryBalance> getCategoryBalances() {
        return categoryBalances;
    }
}
