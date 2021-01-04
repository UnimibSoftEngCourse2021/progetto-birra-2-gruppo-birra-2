package it.progettois.brewday.service;


import it.progettois.brewday.common.converter.BrewerToFatDtoConverter;
import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrewerService {

    private final BrewerRepository brewerRepository;
    private final BrewerToFatDtoConverter brewerToFatDtoConverter;

    @Autowired
    public BrewerService(BrewerRepository brewerRepository, BrewerToFatDtoConverter brewerToFatDtoConverter) {
        this.brewerRepository = brewerRepository;
        this.brewerToFatDtoConverter = brewerToFatDtoConverter;
    }

    public List<BrewerFatDto> getBrewers() {
        return this.brewerRepository.findAll()
                .stream()
                .map(brewerToFatDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public Brewer saveBrewer(Brewer brewer) {
        return this.brewerRepository.save(brewer);
    }
}
