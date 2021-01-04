package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.persistence.model.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BrewerToDtoConverter implements Converter<Brewer, BrewerDto> {

    @Override
    public BrewerDto convert(Brewer brewer) {

        return BrewerDto.builder()
                .brewerId(brewer.getBrewerId())
                .email(brewer.getEmail())
                .name(brewer.getName())
                .username(brewer.getUsername())
                .recipes(brewer.getRecipes().stream().map(Recipe::getRecipeId).collect(Collectors.toList()))
                .storage(brewer.getStorage().stream().map(BrewerIngredient::getBrewerIngredientId).collect(Collectors.toList()))
                .ingredients(brewer.getIngredients().stream().map(Ingredient::getIngredientId).collect(Collectors.toList()))
                .tools(brewer.getTools().stream().map(Tool::getToolId).collect(Collectors.toList()))
                .build();
    }
}
