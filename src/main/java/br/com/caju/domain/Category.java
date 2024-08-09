package br.com.caju.domain;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.List;

@Serdeable
@Entity
@Table(name = "categories")
public class Category {

    @Id
    private String id;

    private boolean active;

    @Type(ListArrayType.class)
    @Column(name = "merchant_category_codes", columnDefinition = "integer[]")
    private List<Integer> merchantCategoryCodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Integer> getMerchantCategoryCodes() {
        return merchantCategoryCodes;
    }

    public void setMerchantCategoryCodes(List<Integer> merchantCategoryCodes) {
        this.merchantCategoryCodes = merchantCategoryCodes;
    }
}
