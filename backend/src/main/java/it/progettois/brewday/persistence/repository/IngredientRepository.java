package it.progettois.brewday.persistence.repository;

import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findIngredientsByBrewerAndQuantityGreaterThan(Brewer brewer, Double greaterThan);

}
