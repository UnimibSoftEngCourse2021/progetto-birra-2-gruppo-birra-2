package it.progettois.brewday.controller;

import it.progettois.brewday.model.Brewer;
import it.progettois.brewday.service.BrewerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class BrewerController {

    private final BrewerService brewerService;

    public BrewerController(BrewerService brewerService) {
        this.brewerService = brewerService;
    }

    @GetMapping("/brewer")
    public ResponseEntity<?> GetBrewers() {

        List<Brewer> brewers = this.brewerService.getBrewers();

        if (brewers.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recipes found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(brewers);
        }
    }

    @PostMapping("/brewer")
    public ResponseEntity<?> saveBrewer(@RequestBody Brewer brewer) {
        return ResponseEntity.status(HttpStatus.OK).body(this.brewerService.saveBrewer(brewer));
    }
}
