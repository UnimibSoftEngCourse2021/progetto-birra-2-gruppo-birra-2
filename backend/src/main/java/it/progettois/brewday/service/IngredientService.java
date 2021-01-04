package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.IngredientToDtoConverter;
import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.persistence.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientToDtoConverter ingredientToDtoConverter;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, IngredientToDtoConverter ingredientToDtoConverter) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientToDtoConverter = ingredientToDtoConverter;}

    public List<IngredientDto> getIngredients() {
        return this.ingredientRepository.findAll()
                .stream()
                .map(ingredientToDtoConverter::convert)
                .collect(Collectors.toList()); }

    public Optional<Ingredient> getIngredient(Integer id) {
        return this.ingredientRepository.findById(id); }

    public Ingredient createIngredient(Ingredient ingredient) { return this.ingredientRepository.save(ingredient); }

    public Boolean deleteIngredient(Integer id) {

       if(this.ingredientRepository.existsById(id)) {
           this.ingredientRepository.deleteById(id);
           return true;
       }
       return false;
    }

    public Boolean editIngredient(Integer id, Ingredient modifiedIngredient){
        if(!this.ingredientRepository.existsById(id)) return false;                 //controllo esistenza ingrediente

        modifiedIngredient.setIngredientId(id);
        this.ingredientRepository.save(modifiedIngredient);

        return true;
    }
}
