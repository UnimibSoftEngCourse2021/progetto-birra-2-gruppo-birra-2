package it.progettois.brewday.common.exception;

public class ToolNotFoundException extends GenericNotFoundException {

    public ToolNotFoundException() {
        super("Tool not found.");
    }

    public ToolNotFoundException(int toolId) {
        super("The tool with id: " + toolId + " does not exist.");
    }
}
