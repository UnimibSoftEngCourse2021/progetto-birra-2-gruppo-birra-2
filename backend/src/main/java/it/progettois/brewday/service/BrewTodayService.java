package it.progettois.brewday.service;

import it.progettois.brewday.common.dto.BrewTodayDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.NoBestRecipeException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import it.progettois.brewday.service.maximizebrew.MaximizeBrewInput;
import it.progettois.brewday.service.maximizebrew.MaximizeBrewOutput;
import it.progettois.brewday.service.maximizebrew.MaximizeBrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrewTodayService {

    private final IngredientService ingredientService;
    private final RecipeRepository recipeRepository;
    private final BrewerRepository brewerRepository;
    private final MaximizeBrewService maximizeBrewService;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    public BrewTodayService(RecipeIngredientRepository recipeIngredientRepository,
                            IngredientService ingredientService,
                            RecipeRepository recipeRepository,
                            BrewerRepository brewerRepository,
                            MaximizeBrewService maximizeBrewService) {
        this.ingredientService = ingredientService;
        this.recipeRepository = recipeRepository;
        this.brewerRepository = brewerRepository;
        this.maximizeBrewService = maximizeBrewService;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public BrewTodayDto find(String username) throws BrewerNotFoundException, NoBestRecipeException {

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        List<Recipe> recipes = this.recipeRepository.findAllByBrewer(brewer);
        Integer maxCapacity = brewer.getMaxBrew();

        //Maximize Brew Input
        List<String> ingredientNames = new ArrayList<>();
        List<Double> storageValues = new ArrayList<>();
        List<Double> ingredientsRatio = new ArrayList<>();

        //Proportion Input Variables
        List<Double> recipeIngredientQuantityValues = new ArrayList<>();

        //This map associates each recipe to how many stored ingredients it can use
        HashMap<Recipe, MaximizeBrewOutput> brewTodayMap = new HashMap<>();

        for (Recipe r : recipes) {

            boolean compatible = true;
            recipeIngredientQuantityValues.clear();
            ingredientNames.clear();
            storageValues.clear();
            ingredientsRatio.clear();
            List<RecipeIngredient> recipeIngredients = this.recipeIngredientRepository.findAllByRecipe(r);

            for (RecipeIngredient i : recipeIngredients) {

                //Checks if the recipe ingredient is available in storage
                if (Boolean.FALSE.equals(this.ingredientService.isInStorage(username, i.getIngredient().getIngredientId()))) {
                    compatible = false;
                    break;
                }

                ingredientNames.add(i.getIngredient().getName());
                storageValues.add(i.getIngredient().getQuantity());
                ingredientsRatio.add(i.getQuantity());
                recipeIngredientQuantityValues.add(i.getQuantity());

            }

            if (compatible) {

                //Find the best ingredient combination for the current recipe
                MaximizeBrewOutput result = maximizeBrew(ingredientsRatio, maxCapacity, ingredientNames, storageValues);
                brewTodayMap.put(r, result);

            } else {
                //Sets the FO value to 0
                MaximizeBrewOutput foZero = new MaximizeBrewOutput();
                foZero.setFo(0.0);
                brewTodayMap.put(r, foZero);
            }
        }

        //Find recipe with max storage usage
        Recipe key = null;
        double max = 0.0;
        for (Map.Entry<Recipe, MaximizeBrewOutput> set : brewTodayMap.entrySet()) {

            if (set.getValue().getFo() > max) {
                max = set.getValue().getFo();
                key = set.getKey();
            }

        }

        if (max == 0.0)
            throw new NoBestRecipeException();

        return BrewTodayDto.builder()
                .recipeId(key.getRecipeId())
                .recipeName(key.getName())
                .recipeDescription(key.getDescription())
                .ingredientQuantities(brewTodayMap.get(key).getIngredients())
                .build();
    }

    //Returns the best quantity combination
    private MaximizeBrewOutput maximizeBrew(List<Double> proportionValues, Integer maxCapacity, List<String> ingredientNames, List<Double> storageValues ) {

        MaximizeBrewInput input = MaximizeBrewInput.builder()
                .capacity(maxCapacity)
                .ingredientNames(ingredientNames)
                .proportions(proportionValues)
                .storage(storageValues)
                .build();
        return this.maximizeBrewService.getMaxBrew(input);
    }

}
