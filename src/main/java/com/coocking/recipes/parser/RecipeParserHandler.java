package com.coocking.recipes.parser;

import com.coocking.recipes.dto.Ingredient;
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
                    case "ing": {
                        Ingredient ingredient = new Ingredient();
                        element.elements().forEach(el -> {
                            Node innerNode = ((Element) el).detach();
                            if ("item".equals(innerNode.getName())) {
                                ingredient.setName(innerNode.getText());
                            } else {
                                Node qty = ((Element) el).element("qty").detach();
                                if (!qty.getText().isBlank()) {
                                    ingredient.setQuantity(qty.getText());
                                }
                                Node unit = ((Element) el).element("unit").detach();
                                if (!unit.getText().isBlank()) {
                                    ingredient.setUnit(unit.getText());
                                }
                            }
                        });
                        recipe.getIngredients().add(ingredient);
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
}
