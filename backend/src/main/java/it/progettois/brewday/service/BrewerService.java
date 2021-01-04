package it.progettois.brewday.service;

import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrewerService {

    private final BrewerRepository brewerRepository;

    @Autowired
    public BrewerService(BrewerRepository brewerRepository) {
        this.brewerRepository = brewerRepository;
    }

    public List<Brewer> getBrewers() {
        return this.brewerRepository.findAll();
    }

    public Brewer saveBrewer(Brewer brewer) {
        return this.brewerRepository.save(brewer);
    }
}
