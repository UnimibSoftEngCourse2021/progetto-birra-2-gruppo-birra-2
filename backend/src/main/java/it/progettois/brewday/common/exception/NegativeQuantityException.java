package it.progettois.brewday.common.exception;

public class NegativeQuantityException extends Exception {

    public NegativeQuantityException() {
        super("The quantity must be greater than zero.");
    }

}
