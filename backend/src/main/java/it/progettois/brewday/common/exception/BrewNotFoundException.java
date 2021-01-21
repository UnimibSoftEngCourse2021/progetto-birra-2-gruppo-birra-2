package it.progettois.brewday.common.exception;

public class BrewNotFoundException extends GenericNotFoundException {

    public BrewNotFoundException() {
        super("Brew not found.");
    }

    public BrewNotFoundException(int brewId) {
        super("The brew with id: " + brewId + " does not exist.");
    }
}
