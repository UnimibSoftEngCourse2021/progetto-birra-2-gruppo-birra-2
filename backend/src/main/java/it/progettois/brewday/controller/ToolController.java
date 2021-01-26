package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.common.exception.GenericNotFoundException;
import it.progettois.brewday.common.exception.NegativeCapacityException;
import it.progettois.brewday.common.exception.NegativeQuantityException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;


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
        try {
            return ResponseEntity.ok(new Response(this.toolService.getTools(this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/tool/{id}")
    public ResponseEntity<Response> getTool(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(new Response(this.toolService.getTool(this.jwtTokenUtil.getUsername(request), id)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }

    @PostMapping("/tool")
    public ResponseEntity<Response> createTool(HttpServletRequest request, @RequestBody ToolDto toolDto) {
        try {
            return ResponseEntity.ok(new Response(this.toolService.createTool(toolDto, this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }


    @DeleteMapping("/tool/{id}")
    public ResponseEntity<Response> deleteTool(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            this.toolService.deleteTool(this.jwtTokenUtil.getUsername(request), id);
            return ResponseEntity.ok(new Response("The tool was deleted successfully from the inventory"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @PutMapping("/tool/{id}")
    public ResponseEntity<Response> editTool(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody ToolDto toolDto) {
        try {
            this.toolService.editTool(this.jwtTokenUtil.getUsername(request), id, toolDto);
            return ResponseEntity.ok(new Response("The tool in your inventory has been updated"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (NegativeQuantityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        } catch (NegativeCapacityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }
}
