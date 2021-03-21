package com.coocking.recipes.parser;

import com.coocking.recipes.dto.Recipe;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Service
@Slf4j
public class XmlDeserializerService {

    public List<Recipe> loadFromResource() throws IOException, DocumentException {
        List<Recipe> recipes = new ArrayList<>();

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("/loadedRecipes/*.xml");
        for (Resource resource : resources) {
            SAXReader reader = new SAXReader();
            RecipeParserHandler recipeParserHandler = new RecipeParserHandler();
            reader.addHandler("/recipeml/recipe/head/title", recipeParserHandler);
            reader.addHandler("/recipeml/recipe/head/categories", recipeParserHandler);
            reader.addHandler("/recipeml/recipe/head/yield", recipeParserHandler);
            reader.addHandler("/recipeml/recipe/ingredients/ing", recipeParserHandler);
            reader.addHandler("/recipeml/recipe/directions/step", recipeParserHandler);
            reader.read(resource.getURL());
            if (recipeParserHandler.isFailed()) {
                throw new InvalidPropertiesFormatException("Error in xml FIle:" + resource);
            }
            recipes.add(recipeParserHandler.getRecipe());
            ;
        }
        return recipes;
    }
}
