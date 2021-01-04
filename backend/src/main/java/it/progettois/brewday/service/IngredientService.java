package it.progettois.brewday.service;

import it.progettois.brewday.model.Ingredient;
import it.progettois.brewday.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) { this.ingredientRepository = ingredientRepository; }

    public List<Ingredient> getIngredients() { return this.ingredientRepository.findAll(); }

    public Optional<Ingredient> getIngredient(Integer id) { return this.ingredientRepository.findById(id); }

    public Ingredient saveIngredient(Ingredient ingredient) { return this.ingredientRepository.save(ingredient); }

    public Boolean deleteIngredient(Integer id) {

       if(this.ingredientRepository.existsById(id)) {
           this.ingredientRepository.deleteById(id);
           return true;
       }
       return false;
    }
}
