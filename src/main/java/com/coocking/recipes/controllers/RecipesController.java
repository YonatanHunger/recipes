package com.coocking.recipes.controllers;

import com.coocking.recipes.dal.RecipesDal;
import com.coocking.recipes.dto.Ingredient;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No souch category: " + category);
        }
    }
}
