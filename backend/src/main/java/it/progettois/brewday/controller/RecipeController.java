package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.*;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
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
    public ResponseEntity<Response> getRecipes(HttpServletRequest request) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        List<RecipeDto> recipes;
        try {
            recipes = this.recipeService.getRecipes(username);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exists"));
        }

        if (recipes == null || recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No recipes found"));
        } else {
            return ResponseEntity.ok(new Response(recipes));
        }
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Response> getRecipe(HttpServletRequest request, @PathVariable Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        RecipeDto recipeDto;
        try {
            recipeDto = this.recipeService.getRecipe(username, id);
            return ResponseEntity.ok(new Response(recipeDto));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The recipe with id " + id + " does not exists"));
        }
    }

    @PostMapping("/recipe")
    public ResponseEntity<Response> saveRecipe(HttpServletRequest request, @RequestBody RecipeDto recipeDto) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            return ResponseEntity.ok(new Response(this.recipeService.saveRecipe(recipeDto, username)));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        } catch (ConversionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("The brewer with username: " + username + " does not exist"));
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }

    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Response> deleteRecipe(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            this.recipeService.deleteRecipe(username, id);
            return ResponseEntity.status(HttpStatus.OK).body(new Response("The recipe was deleted successfully"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The recipe does not exist"));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        }

    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Response> editRecipe(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody RecipeDto recipeDto) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            this.recipeService.editRecipe(username, id, recipeDto);
            return ResponseEntity.ok(new Response("The recipe has been updated successfully"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The recipe does not exist"));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        } catch (IngredientNotFoundException | RecipeIngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/recipe/{id}/ingredient")
    public ResponseEntity<Response> getIngredientsByRecipe(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        List<IngredientDto> ingredientDto;
        try {
            ingredientDto = this.recipeService.getIngredientsByRecipe(username, id);
            return ResponseEntity.ok(new Response(ingredientDto));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The ingredient does not exist"));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }
}
