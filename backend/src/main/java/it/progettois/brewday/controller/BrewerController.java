package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.exception.GenericNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;


@RestController
public class BrewerController {

    private final BrewerService brewerService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public BrewerController(BrewerService brewerService, JwtTokenUtil jwtTokenUtil) {
        this.brewerService = brewerService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/brewer")
    public ResponseEntity<Response> getBrewers() {
        return ResponseEntity.ok(new Response(this.brewerService.getBrewers()));
    }

    @GetMapping("/brewer/{username}")
    public ResponseEntity<Response> getBrewerByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(new Response(this.brewerService.getBrewerByUsername(username)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @PutMapping("/brewer/{username}")
    public ResponseEntity<Response> editBrewer(HttpServletRequest request, @PathVariable String username, @RequestBody BrewerDto brewerDto) {
        try {
            return ResponseEntity.ok(new Response(this.brewerService.editBrewer(username, this.jwtTokenUtil.getUsername(request), brewerDto)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }

    @DeleteMapping("/brewer/{username}")
    public ResponseEntity<Response> deleteBrewer(HttpServletRequest request, @PathVariable String username) {
        try {
            this.brewerService.deleteBrewer(username, this.jwtTokenUtil.getUsername(request));
            return ResponseEntity.ok(new Response("The brewer was deleted successfully"));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }
}
