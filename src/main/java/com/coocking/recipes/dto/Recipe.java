package com.coocking.recipes.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "recipeId")
public class Recipe {
    private String recipeId = UUID.randomUUID().toString();
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Yields is mandatory")
    private Integer yields;
    @NotEmpty(message = "Categories must include at least one category")
    private List<String> categories = new ArrayList<>();
    @NotEmpty(message = "Ingredients must include at least one category")
    @Valid
    private List<Ingredient> ingredients = new ArrayList<>();
    @NotEmpty(message = "directions must include at least one category")
    private List<String> directions = new ArrayList<>();
}
