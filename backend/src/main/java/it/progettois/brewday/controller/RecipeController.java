package it.progettois.brewday.controller;

import it.progettois.brewday.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    @Autowired
    public RecipeService recipeService;

    @GetMapping("/recipe")
    public String getRecipes(){
        return recipeService.getRecipes();
    }
}
