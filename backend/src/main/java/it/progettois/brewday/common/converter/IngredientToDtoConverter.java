package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.persistence.model.Ingredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientToDtoConverter implements Converter<Ingredient, IngredientDto> {

    @Override
    public IngredientDto convert(Ingredient ingredient) {
        return IngredientDto.builder()
                .ingredientId(ingredient.getIngredientId())
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .unit(ingredient.getUnit())
                .type(ingredient.getType())
                .quantity(ingredient.getQuantity())
                .shared(ingredient.getShared())
                .brewerUsername(ingredient.getBrewer().getUsername())
                .build();
    }
}
