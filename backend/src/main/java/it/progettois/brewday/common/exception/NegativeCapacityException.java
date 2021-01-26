package it.progettois.brewday.common.exception;

public class NegativeCapacityException extends Exception {
    public NegativeCapacityException() {

        super("The capacity must be greater than zero.");
    }
}
