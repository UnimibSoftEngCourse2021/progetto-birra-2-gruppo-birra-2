package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.BrewDto;
import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.persistence.model.Brew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class BrewToDtoConverter implements Converter<Brew, BrewDto> {

    private final JsonToRecipeDtoConverter jsonToRecipeDtoConverter;

    @Autowired
    public BrewToDtoConverter(JsonToRecipeDtoConverter jsonToRecipeDtoConverter) {
        this.jsonToRecipeDtoConverter = jsonToRecipeDtoConverter;
    }

    @Override
    public BrewDto convert(Brew brew) {

        RecipeDto recipeDto = this.jsonToRecipeDtoConverter.convert(brew.getRecipe());

        if (recipeDto == null) {
            return null;
        }

        return BrewDto.builder()
                .brewId(brew.getBrewId())
                .quantity(brew.getQuantity())
                .startDate(brew.getStartDate())
                .duration(brew.getDuration())
                .note(brew.getNote())
                .recipe(recipeDto)
                .brewerUsername(brew.getBrewer().getUsername())
                .build();
    }
}
