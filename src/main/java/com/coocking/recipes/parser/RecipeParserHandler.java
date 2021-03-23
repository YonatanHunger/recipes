package com.coocking.recipes.parser;

import com.coocking.recipes.dto.IngredientSection;
import com.coocking.recipes.dto.IngredientQnty;
import com.coocking.recipes.dto.Recipe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.Node;

@Slf4j
public class RecipeParserHandler implements ElementHandler {
    @Getter
    private final Recipe recipe = new Recipe();
    @Getter
    private boolean failed;

    @Override
    public void onStart(ElementPath elementPath) {

    }

    @Override
    public void onEnd(ElementPath elementPath) {
        Element element = elementPath.getCurrent();
        try {
            if (element != null) {
                Node node = element.detach();
                switch (node.getName()) {
                    case "title": {
                        recipe.setTitle(node.getText());
                        break;
                    }
                    case "categories": {
                        element.elements().forEach(cat -> recipe.getCategories().add(((Element) cat).detach().getText()));
                        break;
                    }
                    case "yield": {
                        recipe.setYields(Integer.valueOf(node.getText()));
                        break;
                    }
                    case "ing-div": {
                        Node title = element.element("title").detach();
                        IngredientSection ingredientSection = IngredientSection.builder().section(title.getText()).build();
                        element.elements().forEach(el -> {
                            Node innerNode = ((Element) el).detach();
                            if (innerNode.getName().equals("ing")) {
                                IngredientQnty ingredientQnty = getIngredientQnty((Element) el);
                                ingredientSection.getIngredientQuantities().add(ingredientQnty);
                            }
                        });
                        recipe.getIngredientSections().add(ingredientSection);
                        break;
                    }
                    case "ing": {
                        IngredientSection ingredientSection;
                        if (recipe.getIngredientSections().isEmpty()) {
                            ingredientSection = new IngredientSection();
                            recipe.getIngredientSections().add(ingredientSection);
                        } else {
                            ingredientSection = recipe.getIngredientSections().get(0);
                        }
                        IngredientQnty ingredientQnty = getIngredientQnty(element);
                        ingredientSection.getIngredientQuantities().add(ingredientQnty);
                        break;
                    }
                    case "step": {
                        recipe.getDirections().add(node.getText());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            failed = true;
            log.error("Error on element {}", element, e);
        }
    }

    private IngredientQnty getIngredientQnty(Element element) {
        IngredientQnty ingredientQnty = new IngredientQnty();
        element.elements().forEach(el -> {
            Node innerNode = ((Element) el).detach();
            if ("item".equals(innerNode.getName())) {
                ingredientQnty.setName(innerNode.getText());
            } else {
                Node qty = ((Element) el).element("qty").detach();
                if (!qty.getText().isBlank()) {
                    ingredientQnty.setQuantity(qty.getText());
                }
                Node unit = ((Element) el).element("unit").detach();
                if (!unit.getText().isBlank()) {
                    ingredientQnty.setUnit(unit.getText());
                }
            }
        });
        return ingredientQnty;
    }
}
