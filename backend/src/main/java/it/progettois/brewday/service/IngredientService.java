package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.DtoToIngredientConverter;
import it.progettois.brewday.common.converter.IngredientToDtoConverter;
import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.common.exception.IngredientNotFoundException;
import it.progettois.brewday.common.exception.NegativeQuantityException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Ingredient;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final BrewerRepository brewerRepository;
    private final IngredientToDtoConverter ingredientToDtoConverter;
    private final DtoToIngredientConverter dtoToIngredientConverter;

    private static final String ITEM_FOR_EXCEPTION = "ingredient";

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository,
                             BrewerRepository brewerRepository,
                             IngredientToDtoConverter ingredientToDtoConverter,
                             DtoToIngredientConverter dtoToIngredientConverter) {
        this.ingredientRepository = ingredientRepository;
        this.brewerRepository = brewerRepository;
        this.ingredientToDtoConverter = ingredientToDtoConverter;
        this.dtoToIngredientConverter = dtoToIngredientConverter;
    }

    private Boolean brewerOwnsIngredient(String username, Integer id) throws BrewerNotFoundException,
            IngredientNotFoundException {
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

    public IngredientDto getIngredient(String username, Integer id) throws BrewerNotFoundException,
            IngredientNotFoundException, AccessDeniedException {

        Ingredient ingredient = this.ingredientRepository.findById(id).orElseThrow(IngredientNotFoundException::new);

        if (Boolean.TRUE.equals(brewerOwnsIngredient(username, ingredient) || ingredient.getShared())) {
            return this.ingredientToDtoConverter.convert(ingredient);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);

    }

    public IngredientDto createIngredient(IngredientDto ingredientDto, String username) throws BrewerNotFoundException,
            NegativeQuantityException {

        if (ingredientDto.getQuantity() < 0) {
            throw new NegativeQuantityException();
        }

        if (ingredientDto.getShared() == null) {
            ingredientDto.setShared(false);
        }

        Ingredient ingredient = dtoToIngredientConverter.convert(ingredientDto);
        ingredient.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
        this.ingredientRepository.save(ingredient);
        return this.ingredientToDtoConverter.convert(ingredient);

    }

    public void deleteIngredient(String username, Integer id) throws AccessDeniedException, IngredientNotFoundException,
            BrewerNotFoundException {

        if (Boolean.TRUE.equals(brewerOwnsIngredient(username, id))) {
            this.ingredientRepository.deleteById(id);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);

    }

    public void editIngredient(String username, Integer id, IngredientDto ingredientDto) throws AccessDeniedException,
            IngredientNotFoundException, BrewerNotFoundException, NegativeQuantityException {

        if (ingredientDto.getQuantity() < 0) {
            throw new NegativeQuantityException();
        }

        if (Boolean.TRUE.equals(brewerOwnsIngredient(username, id))) {
            Ingredient ingredient = dtoToIngredientConverter.convert(ingredientDto);
            ingredient.setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
            ingredient.setIngredientId(id);
            this.ingredientRepository.save(ingredient);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);

    }

    public List<IngredientDto> getStorage(String username) throws BrewerNotFoundException {

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);

        List<Ingredient> ingredients = this.ingredientRepository
                .findIngredientsByBrewerAndQuantityGreaterThan(brewer, 0.0);

        return ingredients
                .stream()
                .map(ingredientToDtoConverter::convert)
                .collect(Collectors.toList());

    }

    public IngredientDto getStorageIngredient(String username, Integer ingredientId) throws BrewerNotFoundException,
            IngredientNotFoundException, AccessDeniedException, EmptyStorageException {

        IngredientDto ingredientDto = getIngredient(username, ingredientId);

        //It is necessary to check that the brewer owns the ingredient so that
        //other brewers storage can't be accessed
        if (ingredientDto.getUsername().equals(username)) {
            if (ingredientDto.getQuantity() > 0) {
                return ingredientDto;
            } else
                throw new EmptyStorageException(ingredientDto.getName());
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);

    }

    public void modifyStoredQuantity(String username, Integer ingredientId, IngredientDto ingredientDto) throws AccessDeniedException,
            IngredientNotFoundException, BrewerNotFoundException, NegativeQuantityException {

        if (ingredientDto == null || ingredientDto.getQuantity() == null)
            throw new NullPointerException();

        /*
         If the brewer owns the ingredient this is retrieved from the repository and the quantity is
         modified accordingly to the type of operation that the user wants to make (increment quantity or decrease quantity)
         If the operation is 'decrease quantity' the value of ingredientDto.getQuantity() will be negative (-)
        */
        if (Boolean.TRUE.equals(brewerOwnsIngredient(username, ingredientId))) {
            Ingredient ingredient = this.ingredientRepository.findById(ingredientId).orElseThrow(IngredientNotFoundException::new);
            double storedQuantity = ingredient.getQuantity();
            Double newQuantity = storedQuantity + ingredientDto.getQuantity();
            ingredient.setQuantity(newQuantity);
            this.ingredientRepository.save(ingredient);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);

    }

    public Boolean isInStorage(String username, Integer ingredientId) throws BrewerNotFoundException {
        List<IngredientDto> storage = getStorage(username);
        boolean result = false;

        for (IngredientDto i : storage) {
            if (i.getIngredientId().equals(ingredientId)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
