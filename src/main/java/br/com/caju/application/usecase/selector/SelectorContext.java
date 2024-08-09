package br.com.caju.application.usecase.selector;

import br.com.caju.domain.CategoryBalance;
import br.com.caju.domain.Transaction;

public class SelectorContext {

    private CategoryBalance categoryBalanceToEvaluate;
    private Transaction transaction;
    private CategoryBalance categoryBalanceSelected;
    private boolean isMccChanged;

    public void setCategoryBalanceToEvaluate(CategoryBalance categoryBalanceToEvaluate) {
        this.categoryBalanceToEvaluate = categoryBalanceToEvaluate;
    }

    public CategoryBalance getCategoryBalanceToEvaluate() {
        return categoryBalanceToEvaluate;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setCategoryBalanceSelected(CategoryBalance categoryBalanceSelected) {
        this.categoryBalanceSelected = categoryBalanceSelected;
    }

    public CategoryBalance getCategoryBalanceSelected() {
        return categoryBalanceSelected;
    }

    public void setMccChanged(boolean mccChanged) {
        isMccChanged = mccChanged;
    }

    public boolean isMccChanged() {
        return isMccChanged;
    }
}
