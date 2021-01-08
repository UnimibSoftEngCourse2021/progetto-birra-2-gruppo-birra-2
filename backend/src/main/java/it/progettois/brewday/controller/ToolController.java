package it.progettois.brewday.controller;


import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ToolNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.service.ToolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import static it.progettois.brewday.common.constant.SecurityConstants.HEADER_STRING;

@RestController
public class ToolController {

    private final ToolService toolService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ToolController(ToolService toolService, JwtTokenUtil jwtTokenUtil) {

        this.toolService = toolService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/tool")
    public ResponseEntity<Object> getTools(HttpServletRequest request) {
        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));
        List<ToolDto> tools;
        try{
            tools = this.toolService.getTools(username);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }
        if (tools.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have no tools in your inventory");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(tools);
        }
    }


    @PostMapping("/tool")
    public ResponseEntity<Object> createTool( HttpServletRequest request, @RequestBody ToolDto toolDto) {
        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.toolService.createTool(toolDto, username));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }
    }


    @DeleteMapping("/tool/{id}")
    public ResponseEntity<String> deleteTool(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try{
            this.toolService.deleteTool(username, id);
            return ResponseEntity.status(HttpStatus.OK).body("The tool was deleted successfully from the inventory");
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ToolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The tool does not exist in your inventory");
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }

    }

    @PutMapping("/tool/{id}")
    public ResponseEntity<?> editTool(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody ToolDto toolDto){

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try{
            this.toolService.editTool(username, id, toolDto);
            return ResponseEntity.status(HttpStatus.OK).body("The tool in your inventory has been updated");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ToolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The tool does not exist in your inventory");
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }
    }


}
