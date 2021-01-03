package it.progettois.brewday.controller;

import it.progettois.brewday.service.ToolService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolController {

    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }
}
