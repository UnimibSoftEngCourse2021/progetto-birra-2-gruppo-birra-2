package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.BrewTodayDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.common.exception.NoBestRecipeException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewTodayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static it.progettois.brewday.common.constant.SecurityConstants.HEADER_STRING;

@RestController
public class BrewTodayController {
    BrewTodayService brewTodayService;
    private final JwtTokenUtil jwtTokenUtil;

    public BrewTodayController(BrewTodayService brewTodayService, JwtTokenUtil jwtTokenUtil) {
        this.brewTodayService = brewTodayService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/today")
    public ResponseEntity<Response> whatShouldIBrewToday(HttpServletRequest request) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            BrewTodayDto bestRecipe = this.brewTodayService.find(username);
            return ResponseEntity.ok(new Response(bestRecipe));
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The brewer does not exist"));
        } catch (EmptyStorageException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        } catch (NoBestRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("There is no recipe you can brew"));
        }
    }
}
