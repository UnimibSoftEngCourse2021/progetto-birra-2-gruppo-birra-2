package it.progettois.brewday.persistence.repository;

import it.progettois.brewday.persistence.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Integer> {
}
