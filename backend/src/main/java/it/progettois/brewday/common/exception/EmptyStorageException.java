package it.progettois.brewday.common.exception;

public class EmptyStorageException extends Exception{
    String message;

    public EmptyStorageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(){
        return this.message;
    }
}
