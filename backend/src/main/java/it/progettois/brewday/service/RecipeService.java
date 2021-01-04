package it.progettois.brewday.service;

import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getRecipes() {
        return this.recipeRepository.findAll();
    }

    public Recipe saveRecipe(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }
}
