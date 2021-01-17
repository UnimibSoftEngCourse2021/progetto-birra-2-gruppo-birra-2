package it.progettois.brewday.controller;


import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ToolNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;

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
    public ResponseEntity<Response> getTools(HttpServletRequest request) {
        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));
        List<ToolDto> tools;
        try {
            tools = this.toolService.getTools(username);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        }
        if (tools.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("You have no tools in your inventory"));
        } else {
            return ResponseEntity.ok(new Response(tools));
        }
    }


    @PostMapping("/tool")
    public ResponseEntity<Response> createTool(HttpServletRequest request, @RequestBody ToolDto toolDto) {
        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));
        try {
            return ResponseEntity.ok(new Response(this.toolService.createTool(toolDto, username)));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        }
    }


    @DeleteMapping("/tool/{id}")
    public ResponseEntity<Response> deleteTool(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            this.toolService.deleteTool(username, id);
            return ResponseEntity.ok(new Response("The tool was deleted successfully from the inventory"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (ToolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The tool does not exist in your inventory"));
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        }
    }

    @PutMapping("/tool/{id}")
    public ResponseEntity<Response> editTool(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody ToolDto toolDto) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            this.toolService.editTool(username, id, toolDto);
            return ResponseEntity.ok(new Response("The tool in your inventory has been updated"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (ToolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The tool does not exist in your inventory"));
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        }
    }
}
