package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.DtoToIngredientConverter;
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
    private final DtoToIngredientConverter dtoToIngredientConverter;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, IngredientToDtoConverter ingredientToDtoConverter, DtoToIngredientConverter dtoToIngredientConverter) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientToDtoConverter = ingredientToDtoConverter;
        this.dtoToIngredientConverter = dtoToIngredientConverter;
    }

    public List<IngredientDto> getIngredients() {
        return this.ingredientRepository.findAll()
                .stream()
                .map(ingredientToDtoConverter::convert)
                .collect(Collectors.toList()); }

    public Optional<Ingredient> getIngredient(Integer id) {
        return this.ingredientRepository.findById(id); }

    public Ingredient createIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = dtoToIngredientConverter.convert(ingredientDto);
        return this.ingredientRepository.save(ingredient);
    }

    public Boolean deleteIngredient(Integer id) {

       if(this.ingredientRepository.existsById(id)) {
           this.ingredientRepository.deleteById(id);
           return true;
       }
       return false;
    }

    public Boolean editIngredient(Integer id, IngredientDto ingredientDto){
        if(!this.ingredientRepository.existsById(id)) return false;                 //controllo esistenza ingrediente

        ingredientDto.setIngredientId(id);
        Ingredient ingredient = dtoToIngredientConverter.convert(ingredientDto);    //conversione da dto
        this.ingredientRepository.save(ingredient);

        return true;
    }
}
