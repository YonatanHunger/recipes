package com.coocking.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientSection {
    private String section;
    @NotEmpty
    @Valid
    @Builder.Default
    private List<IngredientQuantity> ingredientQuantities = new ArrayList<>();
}
