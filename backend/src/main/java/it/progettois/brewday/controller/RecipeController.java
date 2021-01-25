package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.RecipeDto;
import it.progettois.brewday.common.exception.ConversionException;
import it.progettois.brewday.common.exception.GenericNotFoundException;
import it.progettois.brewday.common.exception.NegativeQuantityException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;


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
        try {
            return ResponseEntity.ok(new Response(this.recipeService.getRecipes(this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Response> getRecipe(HttpServletRequest request, @PathVariable Integer id) {
        try {
            return ResponseEntity.ok(new Response(this.recipeService.getRecipe(this.jwtTokenUtil.getUsername(request), id)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }

    @PostMapping("/recipe")
    public ResponseEntity<Response> saveRecipe(HttpServletRequest request, @RequestBody RecipeDto recipeDto) {
        try {
            return ResponseEntity.ok(new Response(this.recipeService.saveRecipe(recipeDto, this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (ConversionException|NegativeQuantityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Response> deleteRecipe(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            this.recipeService.deleteRecipe(this.jwtTokenUtil.getUsername(request), id);
            return ResponseEntity.ok(new Response("The recipe was deleted successfully"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Response> editRecipe(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody RecipeDto recipeDto) {
        try {
            this.recipeService.editRecipe(this.jwtTokenUtil.getUsername(request), id, recipeDto);
            return ResponseEntity.ok(new Response("The recipe has been updated successfully"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (NegativeQuantityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/recipe/{id}/ingredient")
    public ResponseEntity<Response> getIngredientsByRecipe(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(new Response(this.recipeService.getIngredientsByRecipe(this.jwtTokenUtil.getUsername(request), id)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }
}
