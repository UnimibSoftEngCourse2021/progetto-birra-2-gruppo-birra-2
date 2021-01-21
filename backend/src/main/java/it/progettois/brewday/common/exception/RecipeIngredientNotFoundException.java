package it.progettois.brewday.common.exception;

public class RecipeIngredientNotFoundException extends GenericNotFoundException {

    public RecipeIngredientNotFoundException() {
        super("The association between this ingredient and this recipe does not exist.");
    }
}
