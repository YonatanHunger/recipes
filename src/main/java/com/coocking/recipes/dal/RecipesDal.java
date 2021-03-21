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

    @Autowired
    public RecipesDal(XmlDeserializerService xmlDeserializerService) throws IOException, DocumentException {
        recipes = xmlDeserializerService.loadFromResource();
        recipes.forEach(recipe -> {
            recipe.getCategories().forEach(category -> recipeByCategory.computeIfAbsent(category, x -> new ArrayList<>()).add(recipe));
            recipesNames.add(recipe.getTitle());
        });
    }
}
