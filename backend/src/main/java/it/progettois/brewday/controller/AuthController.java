package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.exception.ConversionException;
import it.progettois.brewday.service.BrewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final BrewerService brewerService;

    @Autowired
    public AuthController(BrewerService brewerService) {
        this.brewerService = brewerService;
    }

    @PostMapping("/brewer")
    public ResponseEntity<?> registration(@RequestBody BrewerDto brewerDto) {
        try {
            return ResponseEntity.ok(this.brewerService.saveBrewer(brewerDto));
        } catch (ConversionException e) {
            //TODO Gestione migliore errore
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in creating user");
        }
    }
}
