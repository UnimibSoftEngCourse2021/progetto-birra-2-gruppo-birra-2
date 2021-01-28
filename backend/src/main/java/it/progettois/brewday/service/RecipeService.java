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

    private static final String ITEM_FOR_EXCEPTION = "recipe";

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

        List<Recipe> recipes = this.recipeRepository.findAllByBrewer(this.brewerRepository.findByUsername(username)
                .orElseThrow(BrewerNotFoundException::new));

        recipes.forEach(recipe -> recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe)));

        return recipes.stream().map(this.recipeToDtoConverter::convert).collect(Collectors.toList());
    }

    public RecipeDto getRecipe(String username, Integer id) throws BrewerNotFoundException, RecipeNotFoundException, AccessDeniedException {

        Recipe recipe = this.recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);

        recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe));

        if (Boolean.TRUE.equals(brewerOwnsRecipe(username, recipe) || recipe.getShared())) {
            return this.recipeToDtoConverter.convert(recipe);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);
    }

    public List<RecipeDto> getPublicRecipes() throws RecipeNotFoundException {

        List<Recipe> recipes = this.recipeRepository.findBySharedIsTrue();
        if (recipes.isEmpty()) throw new RecipeNotFoundException("There are no public recipes... YET");
        recipes.forEach(recipe -> recipe.setIngredients(this.recipeIngredientRepository.findAllByRecipe(recipe)));
        return recipes.stream().map(this.recipeToDtoConverter::convert).collect(Collectors.toList());

    }

    public RecipeDto saveRecipe(RecipeDto recipeDto, String username) throws BrewerNotFoundException, ConversionException, IngredientNotFoundException, NegativeQuantityException {

        if (recipeDto.getShared() == null) {
            recipeDto.setShared(false);
        }

        Recipe recipe = this.dtoToRecipeConverter.convert(recipeDto);

        if (recipe == null) {
            throw new ConversionException();
        }

        recipe.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
        // Saves the recipe without ingredients
        recipe = this.recipeRepository.save(recipe);

        List<RecipeIngredientDto> recipeIngredientsDto = recipeDto.getIngredients();
        Double totQuantity = 0.0;
        // Recipe Ingredients total quantity
        for (RecipeIngredientDto recipeIngredientDto : recipeIngredientsDto){
            totQuantity += recipeIngredientDto.getQuantity();
        }

        if(totQuantity > 0) {

            List<RecipeIngredient> recipeIngredientsNew = new ArrayList<>();

            for (RecipeIngredientDto recipeIngredientDto : recipeIngredientsDto) {
                if (recipeIngredientDto.getIngredientId() == null)
                    throw new ConversionException();
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipe(recipe);
                recipeIngredient.setIngredient(this.ingredientRepository.findById(recipeIngredientDto.getIngredientId())
                        .orElseThrow(IngredientNotFoundException::new));
                Double ingredientRatio = recipeIngredientDto.getQuantity()/totQuantity;
                recipeIngredient.setQuantity(ingredientRatio);
                recipeIngredient.setOriginalTotQuantity(totQuantity);
                recipeIngredientsNew.add(this.recipeIngredientRepository.save(recipeIngredient));
            }
            recipe.setIngredients(recipeIngredientsNew);
            return this.recipeToDtoConverter.convert(recipe);
        } else {
            throw new NegativeQuantityException();
        }
    }

    public void deleteRecipe(String username, Integer id) throws AccessDeniedException, RecipeNotFoundException, BrewerNotFoundException {

        if (Boolean.TRUE.equals(brewerOwnsRecipe(username, id))) {

            List<RecipeIngredient> recipeIngredients = this.recipeIngredientRepository.findAllByRecipe_RecipeId(id);

            this.recipeIngredientRepository.deleteAll(recipeIngredients);

            this.recipeRepository.deleteById(id);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);

    }

    public void editRecipe(String username, Integer id, RecipeDto recipeDto) throws AccessDeniedException, RecipeNotFoundException, BrewerNotFoundException, IngredientNotFoundException, RecipeIngredientNotFoundException, NegativeQuantityException {

        if (Boolean.FALSE.equals(brewerOwnsRecipe(username, id))) {
            throw new AccessDeniedException(ITEM_FOR_EXCEPTION);
        }

        Recipe recipe = dtoToRecipeConverter.convert(recipeDto);

        if (recipe == null) {
            throw new RecipeNotFoundException();
        }

        recipe.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
        recipe.setRecipeId(id);
        recipe = this.recipeRepository.save(recipe);

        List<RecipeIngredientDto> recipeIngredientsNew = recipeDto.getIngredients();
        Double totQuantity = 0.0;
        // Recipe Ingredients total quantity
        for (RecipeIngredientDto recipeIngredientDto : recipeIngredientsNew){
            totQuantity += recipeIngredientDto.getQuantity();
        }

        List<RecipeIngredient> recipeIngredientsOld = this.recipeIngredientRepository.findAllByRecipe_RecipeId(id);

        // Removes old ingredients from recipe
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

        if (totQuantity > 0) {
            saveRecipeIngredients(recipeIngredientsNew, totQuantity, recipe);
        } else throw new NegativeQuantityException();
    }

    private void saveRecipeIngredients(List<RecipeIngredientDto> recipeIngredients, Double totQuantity, Recipe recipe) throws RecipeIngredientNotFoundException, IngredientNotFoundException {
        for (RecipeIngredientDto recipeIngredientDto : recipeIngredients) {

            RecipeIngredient recipeIngredient;
            if (recipeIngredientDto.getRecipeIngredientId() == null) {
                recipeIngredient = new RecipeIngredient();
            } else {
                recipeIngredient = this.recipeIngredientRepository.findById(recipeIngredientDto.getRecipeIngredientId())
                        .orElseThrow(RecipeIngredientNotFoundException::new);
            }
            Double ingredientRatio = recipeIngredientDto.getQuantity()/totQuantity;
            recipeIngredient.setQuantity(ingredientRatio);
            recipeIngredient.setOriginalTotQuantity(totQuantity);
            recipeIngredient.setIngredient(this.ingredientRepository.findById(recipeIngredientDto.getIngredientId())
                    .orElseThrow(IngredientNotFoundException::new));
            recipeIngredient.setRecipe(recipe);

            this.recipeIngredientRepository.save(recipeIngredient);

        }
    }

    public List<IngredientDto> getIngredientsByRecipe(String username, Integer id) throws BrewerNotFoundException, RecipeNotFoundException, AccessDeniedException, IngredientNotFoundException {

        if (Boolean.TRUE.equals(brewerOwnsRecipe(username, id))) {
            List<IngredientDto> ingredientsDto = new ArrayList<>();
            List<RecipeIngredient> recipeIngredients = this.recipeIngredientRepository.findAllByRecipe_RecipeId(id);

            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                Ingredient ingredient = this.ingredientRepository.findById(recipeIngredient.getIngredient().getIngredientId()).orElseThrow(IngredientNotFoundException::new);
                ingredientsDto.add(this.ingredientToDtoConverter.convert(ingredient));
            }

            return ingredientsDto;
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);
    }
}
