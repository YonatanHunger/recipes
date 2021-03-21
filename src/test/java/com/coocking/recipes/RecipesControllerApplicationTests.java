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
        Optional<Recipe> chili = recipes.stream().filter(recipe -> recipe.getTitle().contains("Chili")).findFirst();
        Assertions.assertTrue(chili.isPresent(), "did not found chili recipe");
        Recipe chiliRecipe = chili.get();
        Assertions.assertEquals(2, chiliRecipe.getCategories().size(), "Unexpected number of categories");
        Assertions.assertEquals(7, chiliRecipe.getIngredients().size(), "Unexpected number of ingredients");
        Assertions.assertEquals(1, chiliRecipe.getDirections().size(), "Unexpected number of directions");
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

}
