package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.DtoToRecipeConverter;
import it.progettois.brewday.common.converter.IngredientToDtoConverter;
import it.progettois.brewday.common.converter.RecipeToDtoConverter;
import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.dto.RecipeIngredientDto;
import it.progettois.brewday.common.exception.*;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.IngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final BrewerRepository brewerRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeToDtoConverter recipeToDtoConverter;
    private final DtoToRecipeConverter dtoToRecipeConverter;
    private final IngredientRepository ingredientRepository;
    private final IngredientToDtoConverter ingredientToDtoConverter;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         RecipeIngredientRepository recipeIngredientRepository,
                         RecipeToDtoConverter recipeToDtoConverter,
                         DtoToRecipeConverter dtoToRecipeConverter,
                         BrewerRepository brewerRepository,
                         IngredientRepository ingredientRepository,
                         IngredientToDtoConverter ingredientToDtoConverter) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeToDtoConverter = recipeToDtoConverter;
        this.dtoToRecipeConverter = dtoToRecipeConverter;
        this.brewerRepository = brewerRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientToDtoConverter = ingredientToDtoConverter;
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

    public RecipeDto saveRecipe(RecipeDto recipeDto, String username) throws BrewerNotFoundException, ConversionException, IngredientNotFoundException {

        if (recipeDto.getShared() == null) {
            recipeDto.setShared(false);
        }

        Recipe recipe = this.dtoToRecipeConverter.convert(recipeDto);

        if (recipe == null) {
            throw new ConversionException();
        }

        recipe.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));

        recipe = this.recipeRepository.save(recipe);

        List<RecipeIngredientDto> recipeIngredientsDto = recipeDto.getIngredients();
        List<RecipeIngredient> recipeIngredientsNew = new ArrayList<>();

        for (RecipeIngredientDto recipeIngredientDto : recipeIngredientsDto) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(this.ingredientRepository.findById(recipeIngredientDto.getIngredientId())
                    .orElseThrow(IngredientNotFoundException::new));
            recipeIngredient.setQuantity(recipeIngredientDto.getQuantity());
            recipeIngredientsNew.add(this.recipeIngredientRepository.save(recipeIngredient));
        }
        recipe.setIngredients(recipeIngredientsNew);
        return this.recipeToDtoConverter.convert(recipe);
    }

    public void deleteRecipe(String username, Integer id) throws AccessDeniedException, RecipeNotFoundException, BrewerNotFoundException {

        if (brewerOwnsRecipe(username, id)) {

            List<RecipeIngredient> recipeIngredients = this.recipeIngredientRepository.findAllByRecipe_RecipeId(id);

            this.recipeIngredientRepository.deleteAll(recipeIngredients);

            this.recipeRepository.deleteById(id);
        } else throw new AccessDeniedException("You don't have permission to delete this recipe");

    }

    public void editRecipe(String username, Integer id, RecipeDto recipeDto) throws AccessDeniedException, RecipeNotFoundException, BrewerNotFoundException, IngredientNotFoundException, RecipeIngredientNotFoundException {

        if (brewerOwnsRecipe(username, id)) {
            Recipe recipe = dtoToRecipeConverter.convert(recipeDto);

            if (recipe == null) {
                throw new RecipeNotFoundException();
            }

            recipe.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
            recipe.setRecipeId(id);
            recipe = this.recipeRepository.save(recipe);

            List<RecipeIngredientDto> recipeIngredientsNew = recipeDto.getIngredients();
            List<RecipeIngredient> recipeIngredientsOld = this.recipeIngredientRepository.findAllByRecipe_RecipeId(id);

            for (RecipeIngredient recipeIngredient : recipeIngredientsOld) {
                boolean isPresent = false;
                for (RecipeIngredientDto recipeIngredientDto : recipeIngredientsNew) {
                    if (recipeIngredient.getRecipeIngredientId().equals(recipeIngredientDto.getRecipeIngredientId())) {
                        isPresent = true;
                        break;
                    }
                }
                if (!isPresent) {
                    this.recipeIngredientRepository.delete(recipeIngredient);
                }
            }

            for (RecipeIngredientDto recipeIngredientDto : recipeIngredientsNew) {

                RecipeIngredient recipeIngredient;
                if (recipeIngredientDto.getRecipeIngredientId() == null) {
                    recipeIngredient = new RecipeIngredient();
                } else {
                    recipeIngredient = this.recipeIngredientRepository.findById(recipeIngredientDto.getRecipeIngredientId())
                            .orElseThrow(RecipeIngredientNotFoundException::new);
                }

                recipeIngredient.setQuantity(recipeIngredientDto.getQuantity());
                recipeIngredient.setIngredient(this.ingredientRepository.findById(recipeIngredientDto.getIngredientId())
                        .orElseThrow(IngredientNotFoundException::new));
                recipeIngredient.setRecipe(recipe);

                this.recipeIngredientRepository.save(recipeIngredient);
            }
        } else throw new AccessDeniedException("You don't have access to this recipe");
    }

    public List<IngredientDto> getIngredientsByRecipe(String username, Integer id) throws BrewerNotFoundException, RecipeNotFoundException, AccessDeniedException, IngredientNotFoundException {

        if (brewerOwnsRecipe(username, id)) {
            List<IngredientDto> ingredientsDto = new ArrayList<>();
            List<RecipeIngredient> recipeIngredients = this.recipeIngredientRepository.findAllByRecipe_RecipeId(id);

            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                Ingredient ingredient = this.ingredientRepository.findById(recipeIngredient.getIngredient().getIngredientId()).orElseThrow(IngredientNotFoundException::new);
                ingredientsDto.add(this.ingredientToDtoConverter.convert(ingredient));
            }


            return ingredientsDto;
        } else throw new AccessDeniedException("You don't have permission to delete this recipe");
    }
}
