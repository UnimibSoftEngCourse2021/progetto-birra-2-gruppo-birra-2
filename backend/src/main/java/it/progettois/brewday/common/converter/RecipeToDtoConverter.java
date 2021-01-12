package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.persistence.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecipeToDtoConverter implements Converter<Recipe, RecipeDto> {

    private final RecipeIngredientToDtoConverter recipeIngredientToDtoConverter;

    @Autowired
    public RecipeToDtoConverter(RecipeIngredientToDtoConverter recipeIngredientToDtoConverter) {
        this.recipeIngredientToDtoConverter = recipeIngredientToDtoConverter;
    }

    @Override
    public RecipeDto convert(Recipe recipe) {

        RecipeDto recipeDto = new RecipeDto();

        recipeDto.setUsername(recipe.getBrewer().getUsername());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setName(recipe.getName());
        recipeDto.setRecipeId(recipe.getRecipeId());
        recipeDto.setShared(recipe.getShared());
        recipeDto.setIngredients(recipe.getIngredients()
                .stream()
                .map(recipeIngredientToDtoConverter::convert)
                .collect(Collectors.toList()));

        return recipeDto;

    }
}
