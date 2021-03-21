package com.coocking.recipes.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
public class Ingredient {
    private Integer quantity;
    private String unit;
    @NotBlank(message = "Ingredient must include name")
    private String name;
}
