package it.progettois.brewday.persistence.repository;

import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {


    List<Recipe> findAllByBrewer(Brewer brewer);

    List<Recipe> findByShared(boolean shared);

    void deleteByBrewer(Brewer brewer);

}
