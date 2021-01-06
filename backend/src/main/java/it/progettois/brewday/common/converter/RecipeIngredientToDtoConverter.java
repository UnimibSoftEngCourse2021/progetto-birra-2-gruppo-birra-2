package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.RecipeIngredientDto;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecipeIngredientToDtoConverter implements Converter<RecipeIngredient, RecipeIngredientDto> {

    /*
    private final IngredientToDtoConverter ingredientToDtoConverter;

    @Autowired
    public RecipeIngredientToDtoConverter(IngredientToDtoConverter ingredientToDtoConverter) {
        this.ingredientToDtoConverter = ingredientToDtoConverter;
    }

     */

    @Override
    public RecipeIngredientDto convert(RecipeIngredient recipeIngredient) {
        return RecipeIngredientDto.builder()
                .ingredientId(recipeIngredient.getIngredient().getIngredientId())
                .quantity(recipeIngredient.getQuantity())
                .build();
    }
}
