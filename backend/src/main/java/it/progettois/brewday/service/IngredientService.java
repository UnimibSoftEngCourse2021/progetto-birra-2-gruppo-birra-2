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

    public List<Ingredient> getIngredients(){ return this.ingredientRepository.findAll(); }

    public Optional<Ingredient> getIngredient(Integer id){ return this.ingredientRepository.findById(id); }

    public Ingredient saveIngredient(Ingredient ingredient){ return this.ingredientRepository.save(ingredient); }

    // bisogna discutere su come gestire gli ingredienti
    // (cioè se renderli visibili solo ai creatori o a tutti gli utenti così da limitare spazio db attraverso il riutilizzo)
    // da cui dipende l'implementazione di 'deleteIngredient' e 'editIngredient'
}
