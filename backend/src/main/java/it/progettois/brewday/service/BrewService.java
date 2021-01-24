package it.progettois.brewday.service;

import it.progettois.brewday.common.converter.BrewToDtoConverter;
import it.progettois.brewday.common.converter.DtoToBrewConverter;
import it.progettois.brewday.common.converter.RecipeIngredientToDtoConverter;
import it.progettois.brewday.common.dto.BrewDto;
import it.progettois.brewday.common.dto.RecipeIngredientDto;
import it.progettois.brewday.common.exception.*;
import it.progettois.brewday.persistence.model.Brew;
import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.persistence.model.RecipeIngredient;
import it.progettois.brewday.persistence.repository.BrewRepository;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.RecipeIngredientRepository;
import it.progettois.brewday.persistence.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BrewService {

    private final BrewerRepository brewerRepository;
    private final BrewRepository brewRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final BrewToDtoConverter brewToDtoConverter;
    private final DtoToBrewConverter dtoToBrewConverter;
    private final RecipeIngredientToDtoConverter recipeIngredientToDtoConverter;

    @Autowired
    public BrewService(BrewerRepository brewerRepository,
                       BrewRepository brewRepository,
                       BrewToDtoConverter brewToDtoConverter,
                       DtoToBrewConverter dtoToBrewConverter,
                       RecipeRepository recipeRepository,
                       RecipeIngredientToDtoConverter recipeIngredientToDtoConverter,
                       RecipeIngredientRepository recipeIngredientRepository) {
        this.brewerRepository = brewerRepository;
        this.brewRepository = brewRepository;
        this.brewToDtoConverter = brewToDtoConverter;
        this.dtoToBrewConverter = dtoToBrewConverter;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientToDtoConverter = recipeIngredientToDtoConverter;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public List<BrewDto> getBrews(String username) throws BrewerNotFoundException {
        return this.brewRepository.findAllByBrewer(this.brewerRepository.findByUsername(username)
                .orElseThrow(BrewerNotFoundException::new))
                .stream().map(this.brewToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public BrewDto getBrew(Integer id, String username) throws AccessDeniedException, BrewNotFoundException {

        Brew brew = this.brewRepository.findById(id).orElseThrow(BrewNotFoundException::new);

        if (brew.getBrewer().getUsername().equals(username)) {
            return this.brewToDtoConverter.convert(brew);
        } else throw new AccessDeniedException("brew");
    }

    public BrewDto saveBrew(BrewDto brewDto, String username) throws BrewerNotFoundException, AccessDeniedException {

        brewDto.setBrewerUsername(username);

        if(brewDto.getStartDate() == null) {
            brewDto.setStartDate(new Timestamp(System.currentTimeMillis()));
        }

        Brew brew = this.dtoToBrewConverter.convert(brewDto);


        if (brew == null || brew.getBrewer() == null) {
            throw new BrewerNotFoundException();
        }

        if (brew.getBrewer().getUsername().equals(username)) {
            return this.brewToDtoConverter.convert(this.brewRepository.save(brew));

        } else throw new AccessDeniedException("brew");
    }

    public Boolean editBrew(BrewDto brewDto, Integer id, String username) throws BrewerNotFoundException, AccessDeniedException, BrewNotFoundException {

        if (!this.brewRepository.findById(id).orElseThrow(BrewNotFoundException::new).getBrewer().getUsername().equals(username)) {
            throw new AccessDeniedException("brew");
        }

        brewDto.setBrewerUsername(username);

        Brew brew = this.dtoToBrewConverter.convert(brewDto);

        if (brew == null || brew.getBrewer() == null) {
            throw new BrewerNotFoundException();
        }

        brew.setBrewId(id);

        return this.brewToDtoConverter.convert(this.brewRepository.save(brew)) != null;
    }

    public boolean deleteBrew(Integer id, String username) throws AccessDeniedException, BrewNotFoundException {

        Brew brew = this.brewRepository.findById(id).orElseThrow(BrewNotFoundException::new);

        if (brew.getBrewer().getUsername().equals(username)) {
            this.brewRepository.deleteById(id);
            return true;
        } else throw new AccessDeniedException("brew");
    }

    public List<RecipeIngredientDto> getIngredientForBrew(Integer recipeId, String username, Integer quantity) throws RecipeNotFoundException, AccessDeniedException, IngredientNotFoundException, NegativeQuantityException {

        if (quantity <= 0) {
            throw new NegativeQuantityException();
        }

        Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow(RecipeNotFoundException::new);

        if (!recipe.getBrewer().getUsername().equals(username)) {
            throw new AccessDeniedException("recipe");
        }

        List<RecipeIngredient> ingredients = this.recipeIngredientRepository.findAllByRecipe(recipe);

        if (ingredients.isEmpty()) {
            throw new IngredientNotFoundException();
        } else {
            recipe.setIngredients(ingredients);
        }


        return recipe.getIngredients()
                .stream()
                .map(recipeIngredient -> {
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipeIngredientId(recipeIngredient.getRecipeIngredientId());
                    ri.setIngredient(recipeIngredient.getIngredient());
                    ri.setRecipe(recipeIngredient.getRecipe());
                    ri.setQuantity(recipeIngredient.getQuantity() * quantity);
                    return ri;
                })
                .map(recipeIngredientToDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
