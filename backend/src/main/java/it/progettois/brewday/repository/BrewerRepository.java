package it.progettois.brewday.repository;

import it.progettois.brewday.model.Brewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrewerRepository extends JpaRepository<Brewer, UUID> {
}
