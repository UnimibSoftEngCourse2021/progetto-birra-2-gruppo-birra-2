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

        return RecipeDto.builder()
                .brewerId(recipe.getBrewer().getBrewerId())
                .description(recipe.getDescription())
                .name(recipe.getName())
                .recipeId(recipe.getRecipeId())
                .version(recipe.getVersion())
                .ingredients(recipe.getIngredients()
                        .stream()
                        .map(recipeIngredientToDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
