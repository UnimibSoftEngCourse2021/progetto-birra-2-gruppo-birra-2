package it.progettois.brewday.controller;


import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.service.ToolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have no tools in your inventory");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(tools);
        }
    }


    @PostMapping("/tool")
    public ResponseEntity<?> createTool(@RequestBody ToolDto toolDto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.toolService.createTool(toolDto));
    }


    @DeleteMapping("/tool/{id}")
    public ResponseEntity<?> deleteTool(@PathVariable("id") Integer id) {

        if(this.toolService.deleteTool(id)){
            return ResponseEntity.status(HttpStatus.OK).body("The tool has been deleted from your inventory");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The tool does not exist in your inventory");
        }
    }

    @PutMapping("/tool/{id}")
    public ResponseEntity<?> editTool(@PathVariable("id") Integer id, @RequestBody ToolDto toolDto){
        if(this.toolService.editTool(id, toolDto)) {
            return ResponseEntity.status(HttpStatus.OK).body("The tool in your inventory has been updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The tool does not exist in your inventory");
        }

    }

}
