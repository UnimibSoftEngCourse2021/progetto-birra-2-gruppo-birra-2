package it.progettois.brewday.common.exception;

public class AlreadyPresentException extends Exception {

    public AlreadyPresentException() {
        super("This user is already present.");
    }

    public AlreadyPresentException(String item) {
        super("This " + item + " is already taken");
    }
}
