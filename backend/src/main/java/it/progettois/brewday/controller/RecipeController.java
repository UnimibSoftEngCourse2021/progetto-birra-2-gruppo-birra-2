package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe")
    public ResponseEntity<?> getRecipes() {

        List<RecipeDto> recipes = this.recipeService.getRecipes();

        if (recipes.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recipes found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        }
    }

    @PostMapping("/recipe")
    public ResponseEntity<?> saveRecipe(@RequestBody Recipe recipe) {
        return ResponseEntity.status(HttpStatus.OK).body(this.recipeService.saveRecipe(recipe));
    }
}
