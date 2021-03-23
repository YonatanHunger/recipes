package com.coocking.recipes.controllers;

import com.coocking.recipes.dto.Recipe;
import com.coocking.recipes.excaptions.NoSuchCategory;
import com.coocking.recipes.excaptions.TitleAlreadyPresent;
import com.coocking.recipes.services.RecipesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("recipes")
@Slf4j
public class RecipesController {

    @Autowired
    private RecipesService recipesService;


    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipesService.getAllRecipes();
    }

    @GetMapping("/{category}")
    public List<Recipe> getAllRecipesByCategory(@PathVariable String category) {
        try {
            return recipesService.getRecipesByCategory(category);
        } catch (NoSuchCategory noSuchCategory) {
            log.info("User tried to Search for Unknown category {}", category);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such category: " + category);
        }
    }

    @GetMapping("/search")
    public Set<Recipe> searchRecipes(@RequestParam String query) {
        Set<Recipe> recipes = recipesService.searchRecipes(query);
        if (recipes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not found recipes containing the query: " + query);
        }
        return recipes;
    }


    @PutMapping()
    public String addNewRecipe(@Valid @RequestBody Recipe recipe) {
        try {
            return recipesService.addNewRecipe(recipe);
        } catch (TitleAlreadyPresent titleAlreadyPresent) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, titleAlreadyPresent.getMessage());
        }
    }
}
