package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.persistence.model.Ingredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToIngredientConverter implements Converter<IngredientDto, Ingredient> {

    @Override
    public Ingredient convert(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();

        if(ingredientDto.getIngredientId() != null)
            ingredient.setIngredientId(ingredientDto.getIngredientId());

        ingredient.setName(ingredientDto.getName());
        ingredient.setDescription(ingredientDto.getDescription());
        ingredient.setUnit(ingredientDto.getUnit());
        ingredient.setType(ingredientDto.getType());

        return ingredient;

    }
}
