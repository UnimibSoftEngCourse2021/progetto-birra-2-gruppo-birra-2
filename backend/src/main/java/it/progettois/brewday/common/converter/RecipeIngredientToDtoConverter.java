package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.RecipeIngredientDto;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecipeIngredientToDtoConverter implements Converter<RecipeIngredient, RecipeIngredientDto> {

    @Override
    public RecipeIngredientDto convert(RecipeIngredient recipeIngredient) {

        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto();

        recipeIngredientDto.setRecipeIngredientId(recipeIngredient.getRecipeIngredientId());
        recipeIngredientDto.setIngredientId(recipeIngredient.getIngredient().getIngredientId());
        recipeIngredientDto.setQuantity(recipeIngredient.getQuantity());
        recipeIngredientDto.setOriginalTotQuantity(recipeIngredient.getOriginalTotQuantity());

        recipeIngredientDto.setIngredientName(recipeIngredient.getIngredient().getName());

        return recipeIngredientDto;
    }
}
