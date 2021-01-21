package it.progettois.brewday.common.exception;

public class IngredientNotFoundException extends GenericNotFoundException {

    public IngredientNotFoundException() {
        super("Ingredient not found.");
    }

    public IngredientNotFoundException(int ingredientId) {
        super("The ingredient with id: " + ingredientId + " does not exist.");
    }
}
