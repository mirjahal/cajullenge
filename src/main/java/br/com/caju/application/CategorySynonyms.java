package br.com.caju.application;

import io.micronaut.context.annotation.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(value = "cajullenge")
public class CategorySynonyms {

    private Map<String, List<String>> categories;

    public void setCategories(Map<String, List<String>> categories) {
        this.categories = categories;
    }

    public Map<String, List<String>> getCategories() {
        return categories;
    }
}
