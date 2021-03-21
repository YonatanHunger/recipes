package com.coocking.recipes.excaptions;

public class TitleAlreadyPresent extends RuntimeException {
    public TitleAlreadyPresent(String title) {
        super("The following title is already in use: " + title);
    }
}
