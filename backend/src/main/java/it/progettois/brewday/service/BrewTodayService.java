package it.progettois.brewday.service;

import it.progettois.brewday.common.dto.BrewTodayDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.common.exception.NoBestRecipeException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewInput;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewOutput;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrewTodayService {

    private final IngredientService ingredientService;
    private final RecipeRepository recipeRepository;
    private final BrewerRepository brewerRepository;
    private final MaximizeBrewService maximizeBrewService;


    public BrewTodayService(IngredientService ingredientService, RecipeRepository recipeRepository, BrewerRepository brewerRepository, ToolService toolService, MaximizeBrewService maximizeBrewService) {
        this.ingredientService = ingredientService;
        this.recipeRepository = recipeRepository;
        this.brewerRepository = brewerRepository;
        this.maximizeBrewService = maximizeBrewService;
    }

    public BrewTodayDto find(String username) throws BrewerNotFoundException, EmptyStorageException, NoBestRecipeException {

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        List<Recipe> recipes = this.recipeRepository.findAllByBrewer(brewer);
        Integer maxCapacity = brewer.getMaxBrew();

        //Maximize Brew Input
        List<String> ingredientNames = null;
        List<Double> storageValues = null;
        List<Double> proportionValues = null;

        //Proportion Input Variables
        List<Integer> recipeIngredientQuantityValues = null;
        Integer recipeQuantity;

        //This map associates each recipe to how many stored ingredients it can use
        HashMap<Recipe, MaximizeBrewOutput> brewTodayMap = new HashMap<>();

        for(Recipe r : recipes){

            Boolean compatible = true;
            recipeQuantity = 0;
            recipeIngredientQuantityValues = null;
            ingredientNames = null;
            storageValues = null;
            proportionValues = null;

            for(RecipeIngredient i : r.getIngredients()){

                //Checks if the recipe ingredient is available in storage
                if(!this.ingredientService.isInStorage(username, i.getIngredient().getIngredientId()))
                    compatible = false;

                ingredientNames.add(i.getIngredient().getName());
                storageValues.add(i.getIngredient().getQuantity());
                recipeIngredientQuantityValues.add(i.getQuantity());
                recipeQuantity += i.getQuantity();
            }

            //Proportions elaboration
            for(Integer ingredientQuantity : recipeIngredientQuantityValues){
                Double ingredientProportion = Double.valueOf(recipeQuantity/ingredientQuantity);
                proportionValues.add(ingredientProportion);
            }

            if(compatible){

                MaximizeBrewInput input = MaximizeBrewInput.builder()
                        .capacity(maxCapacity)
                        .ingredientNames(ingredientNames)
                        .proportions(proportionValues)
                        .storage(storageValues)
                        .build();

                MaximizeBrewOutput result = this.maximizeBrewService.getMaxBrew(input);

                brewTodayMap.put(r, result);

            } else {
                brewTodayMap.put(r, null);
            }
        }

        Recipe key = null;
        Double max = 0.0;
        for (Map.Entry<Recipe, MaximizeBrewOutput> set : brewTodayMap.entrySet()) {

            if(set.getValue().getFO() > max){
                max = set.getValue().getFO();
                key = set.getKey();
            }

        }

        if(max == 0.0)
            throw new NoBestRecipeException();

        BrewTodayDto bestRecipe = BrewTodayDto.builder()
                .recipeName(key.getName())
                .ingredientQuantities(brewTodayMap.get(key).getIngredients())
                .build();

        return bestRecipe;


    }
}
