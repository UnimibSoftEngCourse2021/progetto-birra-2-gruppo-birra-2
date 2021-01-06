package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.common.exception.RecipeNotFoundException;
import it.progettois.brewday.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static it.progettois.brewday.common.constant.SecurityConstants.HEADER_STRING;

@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public RecipeController(RecipeService recipeService, JwtTokenUtil jwtTokenUtil) {
        this.recipeService = recipeService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/recipe")
    public ResponseEntity<?> getRecipes(HttpServletRequest request) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        List<RecipeDto> recipes;
        try {
            recipes = this.recipeService.getRecipes(username);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exists");
        }

        if (recipes == null || recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recipes found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        }
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.recipeService.getRecipesById(id));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The recipe with id " + id + " does not exists");
        }
    }

    @GetMapping("/recipe/brewer/{id}")
    public ResponseEntity<?> getRecipesByBrewer(@PathVariable Integer id) {
        List<RecipeDto> recipes;
        try {
            recipes = this.recipeService.getRecipesByUser(id);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with id " + id + " does not exists");
        }

        if (recipes == null || recipes.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recipes found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        }
    }

    @PostMapping("/recipe")
    public ResponseEntity<?> saveRecipe(@RequestBody RecipeDto recipe) {

        RecipeDto recipeDto = this.recipeService.saveRecipe(recipe);
        if (recipeDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brewer or ingredient not found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
        }
    }
}
