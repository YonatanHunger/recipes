package com.coocking.recipes.services;

import com.coocking.recipes.dal.RecipesDal;
import com.coocking.recipes.dto.Recipe;
import com.coocking.recipes.excaptions.NoSuchCategory;
import com.coocking.recipes.excaptions.TitleAlreadyPresent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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


    public Set<Recipe> searchRecipes(String query) {
        //find keyWords
        Set<Recipe> foundByKeyWords = recipesDal.getRecipesSearchString().getOrDefault(query.trim().toLowerCase(), Collections.emptySet());
        Set<Recipe> candidates = new HashSet<>(recipesDal.getRecipes());
        candidates.removeAll(foundByKeyWords);
        //find free text
        Set<Recipe> foundResults = candidates.stream().filter(recipe -> recipe.getDirections().stream().anyMatch(step -> step.contains(query))).collect(Collectors.toSet());
        foundResults.addAll(foundByKeyWords);
        return foundResults;
    }

    public String addNewRecipe(Recipe recipe) {
        validate(recipe);
        recipe.setRecipeId(UUID.randomUUID().toString());
        recipesDal.addRecipe(recipe);
        return recipe.getRecipeId();
    }

    private void validate(Recipe recipe) {
        if (recipesDal.getRecipesNames().contains(recipe.getTitle())) {
            throw new TitleAlreadyPresent(recipe.getTitle());
        }
    }
}
