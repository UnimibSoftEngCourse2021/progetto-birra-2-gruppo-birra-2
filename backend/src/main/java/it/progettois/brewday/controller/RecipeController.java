package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/recipe")
    public ResponseEntity<?> saveRecipe(@RequestBody Recipe recipe) {
        return ResponseEntity.status(HttpStatus.OK).body(this.recipeService.saveRecipe(recipe));
    }
}
