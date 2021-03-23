package com.coocking.recipes.dal;

import com.coocking.recipes.dto.Recipe;
import com.coocking.recipes.parser.XmlDeserializerService;
import lombok.Getter;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Getter
public class RecipesDal {
    private final List<Recipe> recipes;
    private final Map<String, List<Recipe>> recipeByCategory = new HashMap<>();
    private final Set<String> recipesNames = new HashSet<>();
    private final Map<String, Set<Recipe>> recipesSearchString = new HashMap<>();

    @Autowired
    public RecipesDal(XmlDeserializerService xmlDeserializerService) throws IOException, DocumentException {
        recipes = xmlDeserializerService.loadFromResource();
        recipes.forEach(this::addRecipe);
    }

    public void addRecipe(Recipe recipe) {
        recipe.getCategories().forEach(category -> {
            recipeByCategory.computeIfAbsent(category, x -> new ArrayList<>()).add(recipe);
            recipesSearchString.computeIfAbsent(category.trim().toLowerCase(), x -> new HashSet<>()).add(recipe);
        });
        recipesSearchString.computeIfAbsent(recipe.getTitle().trim().toLowerCase(), x -> new HashSet<>()).add(recipe);
        recipe.getIngredientSections().forEach(ingredient -> {
            ingredient.getIngredientQuantities().forEach(ingredientQnty -> {
                String ingredientName = ingredientQnty.getName();
                if (ingredientName.contains(";")) {
                    ingredientName = ingredientName.substring(0, ingredientName.indexOf(";"));
                }
                recipesSearchString.computeIfAbsent(ingredientName.trim().toLowerCase(), x -> new HashSet<>()).add(recipe);
            });
        });
        recipesNames.add(recipe.getTitle());
    }
}
