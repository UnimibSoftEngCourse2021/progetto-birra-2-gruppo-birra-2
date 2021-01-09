package it.progettois.brewday.service;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BrewTodayService {

    private final IngredientService ingredientService;
    private final RecipeRepository recipeRepository;
    private final BrewerRepository brewerRepository;
    private final ToolService toolService;


    public BrewTodayService(IngredientService ingredientService, RecipeRepository recipeRepository, BrewerRepository brewerRepository, ToolService toolService) {
        this.ingredientService = ingredientService;
        this.recipeRepository = recipeRepository;
        this.brewerRepository = brewerRepository;
        this.toolService = toolService;
    }

    public void find(String username) throws BrewerNotFoundException, EmptyStorageException {

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        List<Recipe> recipes = this.recipeRepository.findAllByBrewer(brewer);
        List<IngredientDto> storage = this.ingredientService.getStorage(username);
        List<ToolDto> tools = this.toolService.getTools(); //TODO passare parametro username
        Integer maxCapacity = 0;

        //This map associates each recipe to how many stored ingredients it can use
        HashMap<Recipe, Double> usedQuantityByRecipes = new HashMap<>();

        //Finds max capacity
        for (ToolDto tool : tools) {
            maxCapacity = maxCapacity + tool.getCapacity();
        }

        for(Recipe r : recipes){
            Boolean compatible = true;
            for(RecipeIngredient d : r.getIngredients()){
                //Checks if the recipe ingredient is available in storage
                if(!this.ingredientService.isInStorage(username, d.getIngredient().getIngredientId()))
                    compatible = false;

            }

            if(compatible){
                //TODO trova max utilizzo ingredienti
                //TODO inserisci max utilizzo nella map
            } else {
                usedQuantityByRecipes.put(r, 0.0);
            }
        }

        //TODO restituisci ricetta con valore associato massimo


    }
}
