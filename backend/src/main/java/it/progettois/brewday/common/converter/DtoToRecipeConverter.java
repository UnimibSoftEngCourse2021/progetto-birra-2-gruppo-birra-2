package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.IngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoToRecipeConverter implements Converter<RecipeDto, Recipe> {

    private final BrewerRepository brewerRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    public DtoToRecipeConverter(BrewerRepository brewerRepository,
                                IngredientRepository ingredientRepository,
                                RecipeIngredientRepository recipeIngredientRepository) {
        this.brewerRepository = brewerRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @Override
    public Recipe convert(RecipeDto recipeDto) {

        Recipe recipe = new Recipe();
        Brewer brewer = this.brewerRepository.findById(recipeDto.getBrewerId()).orElse(null);

        if (brewer == null) {
            return null;
        }

        List<RecipeIngredient> recipeIngredients = recipeDto.getIngredients().stream().map(recipeIngredientDto -> {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            Ingredient ingredient = this.ingredientRepository.findById(recipeIngredientDto.getIngredientId()).orElse(null);
            if (ingredient == null) {
                return null;
            }
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(recipeIngredientDto.getQuantity());
            return recipeIngredient;
        }).collect(Collectors.toList());

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            if (recipeIngredient == null) {
                return null;
            } else {
                this.recipeIngredientRepository.save(recipeIngredient);
            }
        }

        recipe.setVersion(recipeDto.getVersion());
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setShared(recipeDto.getShared());
        recipe.setBrewer(brewer);
        recipe.setIngredients(recipeIngredients);

        return recipe;
    }

}
