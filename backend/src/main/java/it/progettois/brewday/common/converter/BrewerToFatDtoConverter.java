package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.persistence.model.Brewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BrewerToFatDtoConverter implements Converter<Brewer, BrewerFatDto> {

    private final RecipeToDtoConverter recipeToDtoConverter;
    private final BrewerIngredientToDtoConverter brewerIngredientToDtoConverter;
    private final ToolToDtoConverter toolToDtoConverter;
    private final IngredientToDtoConverter ingredientToDtoConverter;


    @Autowired
    public BrewerToFatDtoConverter(RecipeToDtoConverter recipeToDtoConverter,
                                   BrewerIngredientToDtoConverter brewerIngredientToDtoConverter,
                                   ToolToDtoConverter toolToDtoConverter,
                                   IngredientToDtoConverter ingredientToDtoConverter) {
        this.recipeToDtoConverter = recipeToDtoConverter;
        this.brewerIngredientToDtoConverter = brewerIngredientToDtoConverter;
        this.toolToDtoConverter = toolToDtoConverter;
        this.ingredientToDtoConverter = ingredientToDtoConverter;
    }

    @Override
    public BrewerFatDto convert(Brewer brewer) {

        return BrewerFatDto.builder()
                .brewerId(brewer.getBrewerId())
                .email(brewer.getEmail())
                .name(brewer.getName())
                .username(brewer.getUsername())
                .recipes(brewer.getRecipes().stream().map(recipeToDtoConverter::convert).collect(Collectors.toList()))
                .storage(brewer.getStorage().stream().map(brewerIngredientToDtoConverter::convert).collect(Collectors.toList()))
                .ingredients(brewer.getIngredients().stream().map(ingredientToDtoConverter::convert).collect(Collectors.toList()))
                .tools(brewer.getTools().stream().map(toolToDtoConverter::convert).collect(Collectors.toList()))
                .build();
    }
}
