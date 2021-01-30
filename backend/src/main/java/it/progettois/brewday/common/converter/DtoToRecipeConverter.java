package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class DtoToRecipeConverter implements Converter<RecipeDto, Recipe> {

    BrewerRepository brewerRepository;

    @Autowired
    public DtoToRecipeConverter(BrewerRepository brewerRepository) {
        this.brewerRepository = brewerRepository;
    }

    @SneakyThrows
    @Override
    public Recipe convert(RecipeDto recipeDto) {

        Recipe recipe = new Recipe();

        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setShared(recipeDto.getShared());
        recipe.setBrewer(this.brewerRepository.findByUsername(recipeDto.getUsername())
                .orElseThrow(BrewerNotFoundException::new));
        return recipe;
    }

}
