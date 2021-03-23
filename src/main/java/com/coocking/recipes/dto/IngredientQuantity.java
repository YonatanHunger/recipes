package com.coocking.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientQuantity {
    private String quantity;
    private String unit;
    @NotBlank(message = "Ingredient must include name")
    private String name;
}
