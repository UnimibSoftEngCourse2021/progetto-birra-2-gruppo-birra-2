package it.progettois.brewday.common.exception;

public class AccessDeniedException extends Exception {

    public AccessDeniedException() {
        super("You don't have access to this item");
    }

    public AccessDeniedException(String item) {
        super("You don't have access to this " + item + ".");
    }
}
