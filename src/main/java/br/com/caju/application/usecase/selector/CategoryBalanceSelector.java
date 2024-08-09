package br.com.caju.application.usecase.selector;

import java.util.List;

public abstract class CategoryBalanceSelector {

    private CategoryBalanceSelector nextSelector;

    public static CategoryBalanceSelector link(List<CategoryBalanceSelector> chain) {
        CategoryBalanceSelector firstSelector = chain.getFirst();
        CategoryBalanceSelector headSelector = firstSelector;

        for (CategoryBalanceSelector nextInChain : chain) {
            if (!nextInChain.equals(firstSelector)) {
                headSelector.nextSelector = nextInChain;
                headSelector = nextInChain;
            }
        }

        return  firstSelector;
    }

    public abstract boolean select(SelectorContext context);

    protected boolean selectNext(SelectorContext context) {
        if (nextSelector == null) {
            return true;
        }

        return nextSelector.select(context);
    }
}
