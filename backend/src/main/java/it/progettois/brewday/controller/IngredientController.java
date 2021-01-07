package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static it.progettois.brewday.common.constant.SecurityConstants.HEADER_STRING;

@RestController
public class IngredientController {

    private final IngredientService ingredientService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public IngredientController(IngredientService ingredientService, JwtTokenUtil jwtTokenUtil) {
        this.ingredientService = ingredientService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/ingredient")
    public ResponseEntity<?> getIngredients(HttpServletRequest request) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        List<IngredientDto> ingredients;
        try{
            ingredients = this.ingredientService.getIngredients(username);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }

        if (ingredients.isEmpty() || ingredients == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ingredients found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(ingredients);
        }

    }

    @GetMapping("/ingredient/{id}")
    public ResponseEntity<?> getIngredient(@PathVariable("id") Integer id) {

        Optional<Ingredient> ingredient = this.ingredientService.getIngredient(id);

        if (ingredient.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(ingredient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        }
    }

    @PostMapping("/ingredient")
    public ResponseEntity<?> createIngredient(@RequestBody IngredientDto ingredientDto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.ingredientService.createIngredient(ingredientDto));
    }

    //Il delete è da modificare rispetto ai permessi
    @DeleteMapping("/ingredient/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable("id") Integer id) {

        if(this.ingredientService.deleteIngredient(id)){
            return ResponseEntity.status(HttpStatus.OK).body("The ingredient was deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        }
    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<?> editIngredient(@PathVariable("id") Integer id, @RequestBody IngredientDto ingredientDto){
        if(this.ingredientService.editIngredient(id, ingredientDto)) {
            return ResponseEntity.status(HttpStatus.OK).body("The ingredient has been updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        }
    }

    @GetMapping("/storage/{id}")
    public ResponseEntity<?> getStorage(@PathVariable("id") Integer brewerId){
        try {
            List<IngredientDto> ingredientDtos = this.ingredientService.getStorage(brewerId);
            return ResponseEntity.status(HttpStatus.OK).body(ingredientDtos);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer does not exist");
        } catch (EmptyStorageException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The storage is empty");
        }
    }
}
