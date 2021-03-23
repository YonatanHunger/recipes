package com.coocking.recipes;

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
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipesControllerApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    private static final String RECIPES = "/recipes";
    private static final String MICROWAVE_CATEGORY = RECIPES + "/Microwave";
    private static final String FOO_CATEGORY = RECIPES + "/FOO";
    private static final String SEARCH = RECIPES + "/search";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllRecipes() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(RECIPES)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<List<Recipe>>() {
        });
        Assertions.assertEquals(3, recipes.size(), "Unexpected number of recipes");
        //without sections
        Optional<Recipe> chili = recipes.stream().filter(recipe -> recipe.getTitle().contains("Chili")).findFirst();
        Assertions.assertTrue(chili.isPresent(), "did not found chili recipe");
        Recipe chiliRecipe = chili.get();
        Assertions.assertEquals(2, chiliRecipe.getCategories().size(), "Unexpected number of categories");
        Assertions.assertEquals(1, chiliRecipe.getIngredientSections().size(), "Unexpected number of ingredients sections");
        Assertions.assertEquals(7, chiliRecipe.getIngredientSections().get(0).getIngredientQuantities().size(), "Unexpected number of ingredient Quantities ");
        Assertions.assertEquals(1, chiliRecipe.getDirections().size(), "Unexpected number of directions");

        //with sections
        Optional<Recipe> amaretto = recipes.stream().filter(recipe -> recipe.getTitle().contains("Amaretto")).findFirst();
        Assertions.assertTrue(chili.isPresent(), "did not found chili recipe");
        Recipe amarettoRecipe = amaretto.get();
        Assertions.assertEquals(3, amarettoRecipe.getCategories().size(), "Unexpected number of categories");
        Assertions.assertEquals(2, amarettoRecipe.getIngredientSections().size(), "Unexpected number of ingredients sections");
        Assertions.assertEquals(8, amarettoRecipe.getIngredientSections().get(0).getIngredientQuantities().size(), "Unexpected number of ingredient Quantities ");
        Assertions.assertEquals(1, amarettoRecipe.getDirections().size(), "Unexpected number of directions");
    }


    @Test
    void getRecipesByCategory() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(MICROWAVE_CATEGORY)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<List<Recipe>>() {
        });
        Assertions.assertEquals(1, recipes.size(), "Unexpected number of recipes");
        Optional<Recipe> zucchini = recipes.stream().filter(recipe -> recipe.getTitle().contains("Zucchini")).findFirst();
        Assertions.assertTrue(zucchini.isPresent(), "did not found Zucchini recipe");
    }

    @Test
    void getRecipesByCategoryFindNone() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FOO_CATEGORY)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void searchNotFound() throws Exception {
        String category = "Fooo";
        mockMvc.perform(MockMvcRequestBuilders.get(SEARCH).queryParam("query", category)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void searchByCategory() throws Exception {
        String category = "Cakes";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(SEARCH).queryParam("query", category)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<List<Recipe>>() {
        });
        Assertions.assertTrue(recipes.stream().allMatch(recipe -> recipe.getCategories().stream().anyMatch(cat -> cat.equals(category))), "Could not find query in category");
    }

    @Test
    void searchByCategoryAssertCaseInsensitive() throws Exception {
        String category = "    CaKeS            ";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(SEARCH).queryParam("query", category)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<List<Recipe>>() {
        });
        Assertions.assertTrue(recipes.stream().allMatch(recipe -> recipe.getCategories().stream().anyMatch(cat -> cat.equalsIgnoreCase(category.trim()))), "Could not find query in category");
    }


    @Test
    void searchByIngredient() throws Exception {
        String Ingredient = "Vanilla instant pudding";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(SEARCH).queryParam("query", Ingredient)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<List<Recipe>>() {
        });
        Assertions.assertTrue(recipes.stream().allMatch(recipe -> recipe.getIngredientSections().stream().anyMatch(ing -> ing.getIngredientQuantities().stream().anyMatch(ingredientQnty -> ingredientQnty.getName().equalsIgnoreCase(Ingredient.trim())))), "Could not find query in ingredient");
    }

    @Test
    void searchByTitle() throws Exception {
        String title = "Another Zucchini Dish";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(SEARCH).queryParam("query", title)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<List<Recipe>>() {
        });
        Assertions.assertTrue(recipes.stream().allMatch(recipe -> recipe.getTitle().equalsIgnoreCase(title.trim())), "Could not find query in ingredient");
    }


    @Test
    void searchByDirections() throws Exception {
        String freeText = "remove from pan";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(SEARCH).queryParam("query", freeText)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Recipe> recipes = objectMapper.readValue(content, new TypeReference<List<Recipe>>() {
        });
        Assertions.assertTrue(recipes.stream().allMatch(recipe -> recipe.getDirections().stream().anyMatch(step -> step.contains(freeText))), "Could not find query in ingredient");
    }

}
