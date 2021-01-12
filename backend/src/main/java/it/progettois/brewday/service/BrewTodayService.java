package it.progettois.brewday.service;

import it.progettois.brewday.common.dto.BrewTodayDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.common.exception.NoBestRecipeException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewInput;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewOutput;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewService;
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


    public BrewTodayService(RecipeIngredientRepository recipeIngredientRepository, IngredientService ingredientService, RecipeRepository recipeRepository, BrewerRepository brewerRepository, MaximizeBrewService maximizeBrewService) {
        this.ingredientService = ingredientService;
        this.recipeRepository = recipeRepository;
        this.brewerRepository = brewerRepository;
        this.maximizeBrewService = maximizeBrewService;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public BrewTodayDto find(String username) throws BrewerNotFoundException, EmptyStorageException, NoBestRecipeException {

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        List<Recipe> recipes = this.recipeRepository.findAllByBrewer(brewer);
        Integer maxCapacity = brewer.getMaxBrew();

        //Maximize Brew Input
        List<String> ingredientNames = new ArrayList<>();
        List<Double> storageValues =  new ArrayList<>();
        List<Double> proportionValues =  new ArrayList<>();

        //Proportion Input Variables
        List<Double> recipeIngredientQuantityValues =  new ArrayList<>();
        Double recipeTotQuantity;

        //This map associates each recipe to how many stored ingredients it can use
        HashMap<Recipe, MaximizeBrewOutput> brewTodayMap = new HashMap<>();

        for(Recipe r : recipes){

            boolean compatible = true;
            recipeTotQuantity = 0.0;
            recipeIngredientQuantityValues.clear();
            ingredientNames.clear();
            storageValues.clear();
            proportionValues.clear();
            List<RecipeIngredient> recipeIngredients = this.recipeIngredientRepository.findAllByRecipe(r);

            for(RecipeIngredient i : recipeIngredients){

                //Checks if the recipe ingredient is available in storage
                if(!this.ingredientService.isInStorage(username, i.getIngredient().getIngredientId())) {
                    compatible = false;
                    break;
                }

                ingredientNames.add(i.getIngredient().getName());
                storageValues.add(i.getIngredient().getQuantity());
                recipeIngredientQuantityValues.add(Double.valueOf(i.getQuantity()));
                recipeTotQuantity += i.getQuantity();
            }

            if(compatible){

                //Proportions elaboration
                for(Double ingredientQuantity : recipeIngredientQuantityValues){
                    Double ingredientProportion = ingredientQuantity/recipeTotQuantity;
                    proportionValues.add(ingredientProportion);
                }

                MaximizeBrewInput input = MaximizeBrewInput.builder()
                        .capacity(maxCapacity)
                        .ingredientNames(ingredientNames)
                        .proportions(proportionValues)
                        .storage(storageValues)
                        .build();

                MaximizeBrewOutput result = this.maximizeBrewService.getMaxBrew(input);

                brewTodayMap.put(r, result);

            } else {
                //Sets the FO value to 0
                MaximizeBrewOutput foZero = new MaximizeBrewOutput();
                foZero.setFO(0.0);
                brewTodayMap.put(r, foZero);
            }
        }

        Recipe key = null;
        double max = 0.0;
        for (Map.Entry<Recipe, MaximizeBrewOutput> set : brewTodayMap.entrySet()) {

            if(set.getValue().getFO() > max){
                max = set.getValue().getFO();
                key = set.getKey();
            }

        }

        if(max == 0.0)
            throw new NoBestRecipeException();

        return BrewTodayDto.builder()
                .recipeId(key.getRecipeId())
                .recipeName(key.getName())
                .recipeDescription(key.getDescription())
                .ingredientQuantities(brewTodayMap.get(key).getIngredients())
                .build();


    }
}
