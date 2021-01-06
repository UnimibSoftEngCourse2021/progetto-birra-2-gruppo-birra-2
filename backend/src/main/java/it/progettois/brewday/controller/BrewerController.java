package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.service.BrewerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class BrewerController {

    private final BrewerService brewerService;

    public BrewerController(BrewerService brewerService) {
        this.brewerService = brewerService;
    }

    @GetMapping("/brewer")
    public ResponseEntity<?> getBrewers() {

        List<BrewerFatDto> brewers = this.brewerService.getBrewers();

        if (brewers.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No brewers found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(brewers);
        }
    }

    @GetMapping("/brewer/{username}")
    public ResponseEntity<?> getBrewerByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(this.brewerService.getBrewerByUsername(username));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brewer with username: " + username + " does not exists");
        }
    }
}
