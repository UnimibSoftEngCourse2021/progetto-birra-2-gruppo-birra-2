package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.service.ToolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ToolController {

    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping("/tool")
    public ResponseEntity<?> getTools() {
        List<ToolDto> tools = this.toolService.getTools();

        if (tools.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tools found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(tools);
        }
    }
}
