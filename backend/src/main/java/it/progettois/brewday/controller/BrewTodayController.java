package it.progettois.brewday.controller;

import it.progettois.brewday.common.exception.GenericNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewTodayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class BrewTodayController {

    private final BrewTodayService brewTodayService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public BrewTodayController(BrewTodayService brewTodayService, JwtTokenUtil jwtTokenUtil) {
        this.brewTodayService = brewTodayService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/today")
    public ResponseEntity<Response> whatShouldIBrewToday(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(new Response(this.brewTodayService.find(this.jwtTokenUtil.getUsername(request))));
        } catch (GenericNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(e.getMessage()));
        }
    }
}
