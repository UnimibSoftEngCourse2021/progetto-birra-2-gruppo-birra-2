package it.progettois.brewday.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.progettois.brewday.common.dto.BrewDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.persistence.model.Brew;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class DtoToBrewConverter implements Converter<BrewDto, Brew> {

    private final BrewerRepository brewerRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeToDtoConverter recipeToDtoConverter;

    @Autowired
    public DtoToBrewConverter(BrewerRepository brewerRepository,
                              RecipeRepository recipeRepository,
                              RecipeToDtoConverter recipeToDtoConverter,
                              RecipeIngredientRepository recipeIngredientRepository) {
        this.brewerRepository = brewerRepository;
        this.recipeRepository = recipeRepository;
        this.recipeToDtoConverter = recipeToDtoConverter;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @Override
    public Brew convert(BrewDto brewDto) {

        Brew brew = new Brew();

        brew.setQuantity(brewDto.getQuantity());
        brew.setStartDate(brewDto.getStartDate());
        brew.setDuration(brewDto.getDuration());
        brew.setNote(brewDto.getNote());

        Brewer brewer;
        try {
            brewer = this.brewerRepository.findByUsername(brewDto.getBrewerUsername()).orElseThrow(BrewerNotFoundException::new);
        } catch (BrewerNotFoundException e) {
            return null;
        }

        brew.setBrewer(brewer);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Recipe recipe = this.recipeRepository.findById(brewDto.getRecipe().getRecipeId()).orElse(null);
            if (recipe == null) {
                return null;
            }
            recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe));
            brew.setRecipe(objectMapper.writeValueAsString(this.recipeToDtoConverter.convert(recipe)));
        } catch (JsonProcessingException e) {
            return null;
        }

        return brew;
    }
}
