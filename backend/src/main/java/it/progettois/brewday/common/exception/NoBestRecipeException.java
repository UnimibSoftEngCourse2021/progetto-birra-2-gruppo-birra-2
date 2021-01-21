package it.progettois.brewday.common.exception;

public class NoBestRecipeException extends GenericNotFoundException {

    public NoBestRecipeException() {
        super("There is no recipe you can brew.");
    }
}
