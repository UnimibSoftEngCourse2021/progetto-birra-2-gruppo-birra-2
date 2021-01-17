package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.DtoToRecipeConverter;
import it.progettois.brewday.common.converter.RecipeToDtoConverter;
import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.RecipeNotFoundException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
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

    private Boolean brewerOwnsRecipe(String username, Integer id) throws BrewerNotFoundException, RecipeNotFoundException {
        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        return this.recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new).getBrewer().equals(brewer);
    }

    private Boolean brewerOwnsRecipe(String username, Recipe recipe) throws BrewerNotFoundException {
        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        return recipe.getBrewer().equals(brewer);
    }


    public List<RecipeDto> getRecipes(String username) throws BrewerNotFoundException {

        return this.recipeRepository
                .findAllByBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new))
                .stream()
                .peek(recipe -> recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe)))
                .map(this.recipeToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public RecipeDto getRecipe(String username, Integer id) throws BrewerNotFoundException, RecipeNotFoundException, AccessDeniedException {

        Recipe recipe = this.recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);

        recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe));

        if (brewerOwnsRecipe(username, recipe) || recipe.getShared()) {
            return this.recipeToDtoConverter.convert(recipe);
        } else throw new AccessDeniedException("You don't have access to view this recipe");

    }

    public RecipeDto saveRecipe(RecipeDto recipeDto, String username) throws BrewerNotFoundException {
        recipeDto.setUsername(username);

        if (recipeDto.getShared() == null) {
            recipeDto.setShared(false);
        }

        Recipe recipe = this.dtoToRecipeConverter.convert(recipeDto);
        Objects.requireNonNull(recipe).setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));

        recipe = this.recipeRepository.save(recipe);

        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            recipeIngredient.setRecipe(recipe);
            this.recipeIngredientRepository.save(recipeIngredient);
        }
        return this.recipeToDtoConverter.convert(recipe);
    }

    public void deleteRecipe(String username, Integer id) throws AccessDeniedException, RecipeNotFoundException, BrewerNotFoundException {

        if(brewerOwnsRecipe(username, id)) {
            this.recipeRepository.deleteById(id);
        } else throw new AccessDeniedException("You don't have permission to delete this recipe");

    }

    public void editRecipe(String username, Integer id, RecipeDto recipeDto)  throws AccessDeniedException, RecipeNotFoundException, BrewerNotFoundException {

        if (brewerOwnsRecipe(username, id)) {
            Recipe recipe = dtoToRecipeConverter.convert(recipeDto);
            Objects.requireNonNull(recipe).setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException :: new));
            recipe.setRecipeId(id);
            this.recipeRepository.save(recipe);

            recipe.getIngredients().forEach(recipeIngredient -> {
                recipeIngredient.setRecipe(this.recipeRepository.save(recipe));
                this.recipeIngredientRepository.save(recipeIngredient);
            });
        } else throw new AccessDeniedException("You don't have access to this recipe");

    }
}
