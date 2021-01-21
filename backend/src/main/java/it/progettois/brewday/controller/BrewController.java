package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewDto;
import it.progettois.brewday.common.exception.GenericNotFoundException;
import it.progettois.brewday.common.exception.NegativeQuantityException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;


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
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new Response(this.brewService.getBrews(this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/brew/{id}")
    public ResponseEntity<Response> getBrew(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(new Response(this.brewService.getBrew(id, this.jwtTokenUtil.getUsername(request))));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @PostMapping("/brew")
    public ResponseEntity<Response> createBrew(HttpServletRequest request, @RequestBody BrewDto brewDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new Response(this.brewService.saveBrew(brewDto, this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }

    @PutMapping("/brew/{id}")
    public ResponseEntity<Response> editBrew(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody BrewDto brewDto) {
        try {
            if (Boolean.TRUE.equals(this.brewService.editBrew(brewDto, id, this.jwtTokenUtil.getUsername(request)))) {
                return ResponseEntity.ok(new Response("The brew has been updated"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error updating brew, try again later"));
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @DeleteMapping("/brew/{id}")
    public ResponseEntity<Response> deleteBrew(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            if (this.brewService.deleteBrew(id, this.jwtTokenUtil.getUsername(request))) {
                return ResponseEntity.ok(new Response("The brew was deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Error deleting brew, try again later"));
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/brew/{recipeId}/ingredient")
    public ResponseEntity<Response> getIngredientForBrew(HttpServletRequest request, @PathVariable("recipeId") Integer recipeId, @RequestParam Integer quantity) {
        try {
            return ResponseEntity.ok(new Response(this.brewService.getIngredientForBrew(recipeId, this.jwtTokenUtil.getUsername(request), quantity)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (NegativeQuantityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }
}
