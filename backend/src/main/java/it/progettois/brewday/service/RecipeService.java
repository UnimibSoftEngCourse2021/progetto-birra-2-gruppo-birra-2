package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.DtoToRecipeConverter;
import it.progettois.brewday.common.converter.RecipeToDtoConverter;
import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.RecipeNotFoundException;
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
    private final DtoToRecipeConverter dtoToRecipeConverter;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         RecipeIngredientRepository recipeIngredientRepository,
                         RecipeToDtoConverter recipeToDtoConverter,
                         DtoToRecipeConverter dtoToRecipeConverter,
                         BrewerRepository brewerRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeToDtoConverter = recipeToDtoConverter;
        this.dtoToRecipeConverter = dtoToRecipeConverter;
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

    public RecipeDto getRecipesById(Integer id) throws RecipeNotFoundException {

        Recipe recipe = this.recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);

        recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe));

        return this.recipeToDtoConverter.convert(recipe);

    }

    public List<RecipeDto> getRecipesByUser(Integer id) throws BrewerNotFoundException {
        return this.recipeRepository
                .findAllByBrewer(this.brewerRepository.findById(id).orElseThrow(BrewerNotFoundException::new))
                .stream()
                .peek(recipe -> recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe)))
                .map(this.recipeToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        Recipe recipe = this.dtoToRecipeConverter.convert(recipeDto);
        if (recipe == null) {
            return null;
        }

        final Recipe recipeToReturn = this.recipeRepository.save(recipe);

        recipe.getIngredients().forEach(recipeIngredient -> {
            recipeIngredient.setRecipe(recipeToReturn);
            this.recipeIngredientRepository.save(recipeIngredient);
        });

        return this.recipeToDtoConverter.convert(recipeToReturn);
    }
}
