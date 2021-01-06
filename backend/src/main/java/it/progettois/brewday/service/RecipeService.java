package it.progettois.brewday.service;


import it.progettois.brewday.common.converter.RecipeToDtoConverter;
import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final BrewerRepository brewerRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeToDtoConverter recipeToDtoConverter;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         RecipeIngredientRepository recipeIngredientRepository,
                         RecipeToDtoConverter recipeToDtoConverter,
                         BrewerRepository brewerRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeToDtoConverter = recipeToDtoConverter;
        this.brewerRepository = brewerRepository;
    }

    public List<RecipeDto> getRecipes(String username) throws BrewerNotFoundException {

        return this.recipeRepository
                .findAllByBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new))
                .stream()
                .peek(recipe -> recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe)))
                .map(this.recipeToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public Recipe saveRecipe(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }
}
