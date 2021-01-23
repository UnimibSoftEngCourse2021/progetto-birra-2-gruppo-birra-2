package it.progettois.brewday.persistence.repository;

import it.progettois.brewday.persistence.model.Brew;
import it.progettois.brewday.persistence.model.Brewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrewRepository extends JpaRepository<Brew, Integer> {

    List<Brew> findAllByBrewer(Brewer brewer);

    void deleteByBrewer(Brewer brewer);

}
