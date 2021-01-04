package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.BrewerIngredientDto;
import it.progettois.brewday.persistence.model.BrewerIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BrewerIngredientToDtoConverter implements Converter<BrewerIngredient, BrewerIngredientDto> {

    private final IngredientToDtoConverter ingredientToDtoConverter;

    @Autowired
    public BrewerIngredientToDtoConverter(IngredientToDtoConverter ingredientToDtoConverter) {
        this.ingredientToDtoConverter = ingredientToDtoConverter;
    }

    @Override
    public BrewerIngredientDto convert(BrewerIngredient brewerIngredient) {
        return BrewerIngredientDto.builder()
                .ingredient(this.ingredientToDtoConverter.convert(brewerIngredient.getIngredient()))
                .quantity(brewerIngredient.getQuantity())
                .build();
    }
}
