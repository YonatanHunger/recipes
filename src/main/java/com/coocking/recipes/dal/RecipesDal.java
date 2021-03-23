package com.coocking.recipes.dal;

import com.coocking.recipes.dto.Recipe;
import com.coocking.recipes.parser.XmlDeserializerService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.io.IOException;
import java.util.*;

@Service
@Getter
@Slf4j
public class RecipesDal {
    private final List<Recipe> recipes = new ArrayList<>();
    private final Map<String, List<Recipe>> recipeByCategory = new HashMap<>();
    private final Set<String> recipesNames = new HashSet<>();
    private final Map<String, Set<Recipe>> recipesSearchString = new HashMap<>();

    @Autowired
    public RecipesDal(XmlDeserializerService xmlDeserializerService,
                      SmartValidator validator) throws IOException, DocumentException {
        List<Recipe> loadedRecipes = xmlDeserializerService.loadFromResource();
        loadedRecipes.forEach(recipe -> {
            Errors errors = new BeanPropertyBindingResult(recipe, recipe.getClass().getName());
            validator.validate(recipe, errors);
            if (errors.getAllErrors().isEmpty()) {
                addRecipe(recipe);
            } else {
                log.error("Invalid Recipe in resource: {}", recipe);
                throw new RuntimeException(errors.getAllErrors().toString());
            }
        });
    }

    public void addRecipe(Recipe recipe) {
        recipe.setRecipeId(UUID.randomUUID().toString());
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
        recipes.add(recipe);
    }
}
