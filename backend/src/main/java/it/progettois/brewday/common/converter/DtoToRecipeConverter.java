package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.persistence.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class DtoToRecipeConverter implements Converter<RecipeDto, Recipe> {

    @Override
    public Recipe convert(RecipeDto recipeDto) {

        Recipe recipe = new Recipe();

        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setShared(recipeDto.getShared());

        return recipe;
    }

}
