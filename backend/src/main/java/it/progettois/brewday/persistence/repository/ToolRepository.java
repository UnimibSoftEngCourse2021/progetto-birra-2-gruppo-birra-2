package it.progettois.brewday.persistence.repository;


import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ToolRepository extends JpaRepository<Tool, Integer> {

    List<Tool> findToolsByBrewer(Brewer brewer);
}
