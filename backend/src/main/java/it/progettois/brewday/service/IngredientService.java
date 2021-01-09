package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.DtoToIngredientConverter;
import it.progettois.brewday.common.converter.IngredientToDtoConverter;
import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.common.exception.IngredientNotFoundException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
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

    private Boolean brewerOwnsIngredient(String username, Integer id) throws BrewerNotFoundException, IngredientNotFoundException {
        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        return this.ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new).getBrewer().equals(brewer);
    }

    private Boolean brewerOwnsIngredient(String username, Ingredient ingredient) throws BrewerNotFoundException {
        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        return ingredient.getBrewer().equals(brewer);
    }

    public List<IngredientDto> getIngredients(String username) throws BrewerNotFoundException {

        return this.ingredientRepository
                .findAllByBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new))
                .stream()
                .map(ingredientToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public IngredientDto getIngredient(String username, Integer id) throws BrewerNotFoundException, IngredientNotFoundException, AccessDeniedException {

        Ingredient ingredient = this.ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new);

        if(brewerOwnsIngredient(username, ingredient) || ingredient.getShared()){
            return this.ingredientToDtoConverter.convert(ingredient);
        } else throw new AccessDeniedException("You don't have access to this ingredient");

    }

    public IngredientDto createIngredient(IngredientDto ingredientDto, String username) throws BrewerNotFoundException {

        Ingredient ingredient = dtoToIngredientConverter.convert(ingredientDto);
        ingredient.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
        this.ingredientRepository.save(ingredient);
        return this.ingredientToDtoConverter.convert(ingredient);

    }

    public void deleteIngredient(String username, Integer id) throws AccessDeniedException, IngredientNotFoundException, BrewerNotFoundException {

        if(brewerOwnsIngredient(username, id)) {
            this.ingredientRepository.deleteById(id);
        } else throw new AccessDeniedException("You don't have permission to delete this ingredient");

    }

    public void editIngredient(String username, Integer id, IngredientDto ingredientDto)  throws AccessDeniedException, IngredientNotFoundException, BrewerNotFoundException {

        if(brewerOwnsIngredient(username, id)) {
            Ingredient ingredient = dtoToIngredientConverter.convert(ingredientDto);
            ingredient.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
            ingredient.setIngredientId(id);
            this.ingredientRepository.save(ingredient);
        } else throw new AccessDeniedException("You don't have access to this ingredient");

    }

    public List<IngredientDto> getStorage(String username) throws BrewerNotFoundException, EmptyStorageException {

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);

        List<Ingredient> ingredients = this.ingredientRepository
                .findIngredientsByBrewerAndQuantityGreaterThan(brewer, 0.0);

        if(ingredients.size() > 0){
            return ingredients
                    .stream()
                    .map(ingredientToDtoConverter::convert)
                    .collect(Collectors.toList());
        } else {
            throw new EmptyStorageException("The storage is empty");
        }

    }

    public IngredientDto getStorageIngredient(String username, Integer ingredientId) throws BrewerNotFoundException, IngredientNotFoundException, AccessDeniedException, EmptyStorageException{

        IngredientDto ingredientDto = getIngredient(username, ingredientId);

        //It is necessary to check that the brewer owns the ingredient so that
        //other brewers storage can't be accessed
        if(ingredientDto.getBrewerUsername().equals(username)) {
            if (ingredientDto.getQuantity() > 0) {
                return ingredientDto;
            } else
                throw new EmptyStorageException("You don't have ingredient: \"" + ingredientDto.getName() + "\" in the storage");
        } else throw new AccessDeniedException("Access Denied");

    }

    public void addToStorage(String username, Integer ingredientId, Double newQuantity) throws AccessDeniedException, IngredientNotFoundException, BrewerNotFoundException, InvalidPropertiesFormatException {

        if(newQuantity == null || newQuantity < 0.0) throw new InvalidPropertiesFormatException("The set quantity is invalid");

        if(brewerOwnsIngredient(username, ingredientId)) {
            Ingredient ingredient = this.ingredientRepository.findById(ingredientId).orElseThrow(IngredientNotFoundException::new);
            ingredient.setQuantity(newQuantity);
            this.ingredientRepository.save(ingredient);
        } else throw new AccessDeniedException("You don't have access to this ingredient");

    }
}
