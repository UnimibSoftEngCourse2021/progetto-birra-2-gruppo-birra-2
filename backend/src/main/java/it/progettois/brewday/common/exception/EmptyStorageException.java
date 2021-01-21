package it.progettois.brewday.common.exception;

public class EmptyStorageException extends GenericNotFoundException {

    public EmptyStorageException() {
        super("The storage is empty");
    }

    public EmptyStorageException(String ingredientName) {
        super("You don't have ingredient: \"" + ingredientName + "\" in the storage");
    }
}
