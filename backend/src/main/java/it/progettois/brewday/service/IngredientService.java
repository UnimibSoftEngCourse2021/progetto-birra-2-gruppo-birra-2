package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.DtoToIngredientConverter;
import it.progettois.brewday.common.converter.IngredientToDtoConverter;
import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final BrewerRepository brewerRepository;
    private final IngredientToDtoConverter ingredientToDtoConverter;
    private final DtoToIngredientConverter dtoToIngredientConverter;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, BrewerRepository brewerRepository, IngredientToDtoConverter ingredientToDtoConverter, DtoToIngredientConverter dtoToIngredientConverter) {
        this.ingredientRepository = ingredientRepository;
        this.brewerRepository = brewerRepository;
        this.ingredientToDtoConverter = ingredientToDtoConverter;
        this.dtoToIngredientConverter = dtoToIngredientConverter;
    }

    public List<IngredientDto> getIngredients(String username) throws BrewerNotFoundException {

        return this.ingredientRepository
                .findAllByBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new))
                .stream()
                .map(ingredientToDtoConverter::convert)
                .collect(Collectors.toList());
    }

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

    public List<IngredientDto> getStorage(Integer brewerId) throws BrewerNotFoundException, EmptyStorageException {
        Brewer brewer = this.brewerRepository.findById(brewerId).orElseThrow(BrewerNotFoundException::new);

        List<Ingredient> ingredients;

        ingredients = this.ingredientRepository
                .findIngredientsByBrewerAndQuantityGreaterThan(brewer, 0.0);

        if(ingredients.size() > 0){
            return ingredients
                    .stream()
                    .map(ingredientToDtoConverter::convert)
                    .collect(Collectors.toList());
        } else {
            throw new EmptyStorageException();
        }




    }
}
