package com.coocking.recipes.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IngredientQnty {
    private String quantity;
    private String unit;
    @NotBlank(message = "Ingredient must include name")
    private String name;
}
