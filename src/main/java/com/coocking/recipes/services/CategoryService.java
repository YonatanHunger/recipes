package com.coocking.recipes.services;

import com.coocking.recipes.dal.RecipesDal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private RecipesDal recipesDal;

    public Set<String> getAllCategories() {
        return recipesDal.getRecipeByCategory().keySet();
    }

}
