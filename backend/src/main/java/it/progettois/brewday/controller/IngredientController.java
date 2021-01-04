package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) { this.ingredientService = ingredientService; }

    @GetMapping("/ingredient")
    public ResponseEntity<?> getIngredients() {

        List<IngredientDto> ingredients = this.ingredientService.getIngredients();

        if (ingredients.size() == 0) {
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
    public ResponseEntity<?> createIngredient(@RequestBody Ingredient ingredient) {
        return ResponseEntity.status(HttpStatus.OK).body(this.ingredientService.createIngredient(ingredient));
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
    public ResponseEntity<?> editIngredient(@PathVariable("id") Integer id, @RequestBody Ingredient modifiedIngredient){
        if(this.ingredientService.editIngredient(id, modifiedIngredient)) {
            return ResponseEntity.status(HttpStatus.OK).body("The ingredient has been updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        }

    }
}
