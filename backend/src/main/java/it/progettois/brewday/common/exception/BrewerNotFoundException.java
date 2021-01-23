package it.progettois.brewday.common.exception;

public class BrewerNotFoundException extends GenericNotFoundException {

    public BrewerNotFoundException() {
        super("Brewer not found.");
    }

    public BrewerNotFoundException(String username) {
        super("The brewer with username: " + username + " does not exist.");
    }
}
