package com.coocking.recipes.services;

import com.coocking.recipes.dal.RecipesDal;
import com.coocking.recipes.dto.Recipe;
import com.coocking.recipes.excaptions.NoSuchCategory;
import com.coocking.recipes.excaptions.TitleAlreadyPresent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class RecipesService {
    @Autowired
    private RecipesDal recipesDal;

    public List<Recipe> getAllRecipes() {
        return recipesDal.getRecipes();
    }

    public List<Recipe> getRecipesByCategory(String category) {
        if (!recipesDal.getRecipeByCategory().containsKey(category)) {
            throw new NoSuchCategory(category);
        }
        return recipesDal.getRecipeByCategory().get(category);
    }

    public void saveRecipe(@Valid Recipe recipe) {
        validate(recipe);
    }

    private void validate(Recipe recipe) {
        if (recipesDal.getRecipesNames().contains(recipe.getTitle())) {
            throw new TitleAlreadyPresent(recipe.getTitle());
        }
    }


}
