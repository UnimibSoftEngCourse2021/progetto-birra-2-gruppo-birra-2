package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.exception.GenericNotFoundException;
import it.progettois.brewday.common.exception.NegativeQuantityException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;


@RestController
public class IngredientController {

    private final IngredientService ingredientService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public IngredientController(IngredientService ingredientService, JwtTokenUtil jwtTokenUtil) {
        this.ingredientService = ingredientService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    //get all brewer ingredients
    @GetMapping("/ingredient")
    public ResponseEntity<Response> getIngredients(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(new Response(this.ingredientService.getIngredients(this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    //get brewer ingredient by IngredientId
    @GetMapping("/ingredient/{id}")
    public ResponseEntity<Response> getIngredient(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(new Response(this.ingredientService.getIngredient(this.jwtTokenUtil.getUsername(request), id)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }

    @PostMapping("/ingredient")
    public ResponseEntity<Response> createIngredient(HttpServletRequest request, @RequestBody IngredientDto ingredientDto) {
        try {
            return ResponseEntity.ok(new Response(this.ingredientService.createIngredient(ingredientDto, this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (NegativeQuantityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }

    @DeleteMapping("/ingredient/{id}")
    public ResponseEntity<Response> deleteIngredient(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            this.ingredientService.deleteIngredient(this.jwtTokenUtil.getUsername(request), id);
            return ResponseEntity.ok(new Response("The ingredient was deleted successfully"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<Response> editIngredient(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody IngredientDto ingredientDto) {
        try {
            this.ingredientService.editIngredient(this.jwtTokenUtil.getUsername(request), id, ingredientDto);
            return ResponseEntity.ok(new Response("The ingredient has been updated"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (NegativeQuantityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/storage")
    public ResponseEntity<Response> getStorage(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(new Response(this.ingredientService.getStorage(this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/storage/{id}")
    public ResponseEntity<Response> getStorageIngredient(HttpServletRequest request, @PathVariable("id") Integer ingredientId) {
        try {
            return ResponseEntity.ok(new Response(this.ingredientService.getStorageIngredient(this.jwtTokenUtil.getUsername(request), ingredientId)));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        }
    }

    // This endpoint increments or decreases the quantity of an owned ingredient
    // ingredientDto.quantity > 0 -> (+)
    // ingredientDto.quantity < 0 -> (-)
    @PutMapping("/storage/{id}")
    public ResponseEntity<Response> modifyStorage(HttpServletRequest request, @PathVariable("id") Integer ingredientId, @RequestBody IngredientDto ingredientDto) {
        try {
            this.ingredientService.modifyStoredQuantity(this.jwtTokenUtil.getUsername(request), ingredientId, ingredientDto);
            return ResponseEntity.ok(new Response("The ingredient has been updated"));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(e.getMessage()));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (NegativeQuantityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }
}
