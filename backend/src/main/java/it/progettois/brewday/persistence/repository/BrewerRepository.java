package it.progettois.brewday.persistence.repository;

import it.progettois.brewday.persistence.model.Brewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrewerRepository extends JpaRepository<Brewer, Integer> {

    Optional<Brewer> findByUsername(String username);
}
