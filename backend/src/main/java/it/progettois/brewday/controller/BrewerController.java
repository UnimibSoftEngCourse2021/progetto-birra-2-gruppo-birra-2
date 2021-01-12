package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.service.BrewerService;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewInput;
import it.progettois.brewday.service.maximizeBrew.MaximizeBrewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
public class BrewerController {

    private final BrewerService brewerService;
    private final MaximizeBrewService maximizeBrewService;

    public BrewerController(BrewerService brewerService, MaximizeBrewService maximizeBrewService) {
        this.brewerService = brewerService;
        this.maximizeBrewService = maximizeBrewService;
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

    @GetMapping("/test")
    public ResponseEntity<?> test() {

        MaximizeBrewInput maximizeBrewInput = MaximizeBrewInput
                .builder()
                .capacity(10)
                .ingredients(Arrays.asList("Acqua", "malto", "luppolo", "altro"))
                .proportions(Arrays.asList(0.25, 0.25, 0.25, 0.25))
                .storage(Arrays.asList(3, 5, 2, 10))
                .build();

        return ResponseEntity.ok(this.maximizeBrewService.getMaxBrew(maximizeBrewInput));
    }
}
