package it.progettois.brewday.repository;

import it.progettois.brewday.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Integer> {
}
