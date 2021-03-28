package com.coocking.recipes;

import com.coocking.recipes.dto.IngredientQuantity;
import com.coocking.recipes.dto.IngredientSection;
import com.coocking.recipes.dto.Recipe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static com.coocking.recipes.RecipesControllerApplicationTests.SEARCH;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddNewRecipesControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private static final String RECIPES = "/recipes";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addNewRecipe() throws Exception {
        Recipe recipe = getValidRecipe();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String id = result.getResponse().getContentAsString();

        result = mockMvc.perform(MockMvcRequestBuilders.get(RECIPES)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> allRecipes = objectMapper.readValue(content, new TypeReference<>() {
        });
        Assertions.assertEquals(4, allRecipes.size(), "New recipe was not added to recipes");

        result = mockMvc.perform(MockMvcRequestBuilders.get(SEARCH).queryParam("query", recipe.getTitle())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<>() {
        });
        Assertions.assertTrue(recipes.stream().allMatch(foundRecipe -> foundRecipe.getRecipeId().equals(id)), "Could not find the created recipe");
    }


    @Test
    void addNewRecipeBadRequestOnTitleNull() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setTitle(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnIdNotNull() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setRecipeId("asd");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnYieldsNull() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setYields(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnCategoriesNull() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setCategories(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnCategoriesEmpty() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setCategories(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnIngredientsEmpty() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setIngredientSections(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnIngredientsNull() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setIngredientSections(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnDirectionsEmpty() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setDirections(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    void addNewRecipeBadRequestOnDirectionsNull() throws Exception {
        Recipe recipe = getValidRecipe();
        recipe.setDirections(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnIngredientQuantityNull() throws Exception {
        Recipe recipe = getValidRecipe();
        IngredientSection ingSection = recipe.getIngredientSections().get(0);
        ingSection.setIngredientQuantities(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    void addNewRecipeBadRequestOnIngredientQuantityEmpty() throws Exception {
        Recipe recipe = getValidRecipe();
        IngredientSection ingSection = recipe.getIngredientSections().get(0);
        ingSection.setIngredientQuantities(List.of());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnIngredientQuantityNameNull() throws Exception {
        Recipe recipe = getValidRecipe();
        IngredientSection ingSection = recipe.getIngredientSections().get(0);
        ingSection.getIngredientQuantities().get(0).setName(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeBadRequestOnIngredientQuantityNameEmpty() throws Exception {
        Recipe recipe = getValidRecipe();
        IngredientSection ingSection = recipe.getIngredientSections().get(0);
        ingSection.getIngredientQuantities().get(0).setName("   ");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void addNewRecipeConflictRequestOnSameTitle() throws Exception {
        Recipe recipe = getValidRecipe();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        result = mockMvc.perform(MockMvcRequestBuilders.put(RECIPES).content(objectMapper.writeValueAsBytes(recipe))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    private Recipe getValidRecipe() {
        Recipe recipe = new Recipe();
        recipe.setTitle(UUID.randomUUID().toString());
        recipe.setYields(4);
        recipe.setCategories(List.of("foo"));
        recipe.setDirections(List.of("bar bar"));
        IngredientSection ingredientSection = IngredientSection.builder().section("bbaarr").build();
        ingredientSection.setIngredientQuantities(List.of(IngredientQuantity.builder().name("onion").build()));
        recipe.setIngredientSections(List.of(ingredientSection));
        return recipe;
    }

}
