package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewDto;
import it.progettois.brewday.common.dto.RecipeIngredientDto;
import it.progettois.brewday.common.exception.BrewNotFoundException;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.IngredientNotFoundException;
import it.progettois.brewday.common.exception.RecipeNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;

import static it.progettois.brewday.common.constant.SecurityConstants.HEADER_STRING;

@RestController
public class BrewController {

    private final BrewService brewService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public BrewController(BrewService brewService, JwtTokenUtil jwtTokenUtil) {
        this.brewService = brewService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/brew")
    public ResponseEntity<Response> getBrews(HttpServletRequest request) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        List<BrewDto> brews;
        try {
            brews = this.brewService.getBrews(username);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        }

        if (brews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No brews found"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new Response(brews));
        }

    }

    @GetMapping("/brew/{id}")
    public ResponseEntity<Response> getBrew(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        BrewDto brewDto;
        try {
            brewDto = this.brewService.getBrew(id, username);
            return ResponseEntity.ok(new Response(brewDto));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (BrewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brew does not exist"));
        }
    }

    @PostMapping("/brew")
    public ResponseEntity<Response> createBrew(HttpServletRequest request, @RequestBody BrewDto brewDto) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            return ResponseEntity.status(HttpStatus.OK).body(new Response(this.brewService.saveBrew(brewDto, username)));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }

    @PutMapping("/brew/{id}")
    public ResponseEntity<Response> editBrew(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody BrewDto brewDto) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            if (this.brewService.editBrew(brewDto, id, username)) {
                return ResponseEntity.status(HttpStatus.OK).body(new Response("The brew has been updated"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error updating brew, try again later"));
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (BrewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brew does not exist"));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer with username: " + username + " does not exist"));
        }
    }

    @DeleteMapping("/brew/{id}")
    public ResponseEntity<Response> deleteBrew(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            boolean result = this.brewService.deleteBrew(id, username);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK).body(new Response("The brew was deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error deleting brew, try again later"));
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (BrewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brew does not exist"));
        }
    }

    @GetMapping("/brew/{recipeId}/ingredient")
    public ResponseEntity<Response> getIngredientForBrew(HttpServletRequest request, @PathVariable("recipeId") Integer recipeId, @RequestParam Integer quantity) {

        if (quantity <= 0) {
            return ResponseEntity.badRequest().body(new Response("The quantity must be greater than zero"));
        }

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            List<RecipeIngredientDto> ingredients = this.brewService.getIngredientForBrew(recipeId, username, quantity);
            return ResponseEntity.ok(new Response(ingredients));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The recipe does not exist"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The ingredients does not exist"));
        }
    }
}
