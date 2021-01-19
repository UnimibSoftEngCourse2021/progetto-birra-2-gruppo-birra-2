package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;

import static it.progettois.brewday.common.constant.SecurityConstants.HEADER_STRING;


@RestController
public class BrewerController {

    private final BrewerService brewerService;
    private final JwtTokenUtil jwtTokenUtil;


    public BrewerController(BrewerService brewerService, JwtTokenUtil jwtTokenUtil) {
        this.brewerService = brewerService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/brewer")
    public ResponseEntity<Response> getBrewers() {

        List<BrewerFatDto> brewers = this.brewerService.getBrewers();

        if (brewers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No brewers found"));
        } else {
            return ResponseEntity.ok(new Response(brewers));
        }
    }

    @GetMapping("/brewer/{username}")
    public ResponseEntity<Response> getBrewerByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(new Response(this.brewerService.getBrewerByUsername(username)));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Brewer with username: " + username + " does not exists"));
        }
    }

    @PutMapping("/brewer/{username}")
    public ResponseEntity<Response> editBrewer(HttpServletRequest request, @PathVariable String username, @RequestBody BrewerDto brewerDto) {

        String usernameFromToken = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            return ResponseEntity.ok(new Response(this.brewerService.editBrewer(username, usernameFromToken, brewerDto)));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Brewer with username: " + username + " does not exists"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }
}
