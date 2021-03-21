package com.coocking.recipes.excaptions;

public class NoSuchCategory extends RuntimeException {
    public NoSuchCategory(String category) {
        super("Could not find category: " + category);
    }
}
