package it.progettois.brewday.common.exception;

public class RecipeNotFoundException extends GenericNotFoundException {

    public RecipeNotFoundException() {
        super("Recipe not found.");
    }

    public RecipeNotFoundException(int recipeId) {
        super("The recipe with id: " + recipeId + " does not exist.");
    }
}
