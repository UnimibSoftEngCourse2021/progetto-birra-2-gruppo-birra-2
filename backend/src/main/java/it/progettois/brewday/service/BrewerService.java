package it.progettois.brewday.service;


import it.progettois.brewday.common.converter.BrewerToFatDtoConverter;
import it.progettois.brewday.common.converter.DtoToBrewerConverter;
import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.exception.AlreadyPresentException;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ConversionException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class BrewerService implements UserDetailsService {

    private final BrewerRepository brewerRepository;
    private static final String ITEM_FROM_EXCEPTION = "brewer";
    private final IngredientRepository ingredientRepository;
    private final ToolRepository toolRepository;
    private final RecipeRepository recipeRepository;
    private final BrewRepository brewRepository;
    private final BrewerToFatDtoConverter brewerToFatDtoConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DtoToBrewerConverter dtoToBrewerConverter;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    public BrewerService(BrewerRepository brewerRepository,
                         IngredientRepository ingredientRepository,
                         ToolRepository toolRepository,
                         RecipeRepository recipeRepository,
                         BrewRepository brewRepository,
                         RecipeIngredientRepository recipeIngredientRepository,
                         BrewerToFatDtoConverter brewerToFatDtoConverter,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         DtoToBrewerConverter dtoToBrewerConverter) {
        this.brewerRepository = brewerRepository;
        this.ingredientRepository = ingredientRepository;
        this.toolRepository = toolRepository;
        this.recipeRepository = recipeRepository;
        this.brewRepository = brewRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.brewerToFatDtoConverter = brewerToFatDtoConverter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.dtoToBrewerConverter = dtoToBrewerConverter;
    }

    public List<BrewerFatDto> getBrewers() {
        return this.brewerRepository.findAll()
                .stream()
                .map(brewerToFatDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public BrewerFatDto getBrewerByUsername(String username) throws BrewerNotFoundException {
        return this.brewerToFatDtoConverter.convert(this.brewerRepository.findByUsername(username)
                .orElseThrow(BrewerNotFoundException::new));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Brewer brewer = this.brewerRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));

        return new User(brewer.getUsername(), brewer.getPassword(), emptyList());
    }

    public BrewerFatDto saveBrewer(BrewerDto brewerDto) throws ConversionException, AlreadyPresentException {

        if (this.brewerRepository.findByUsername(brewerDto.getUsername()).isPresent()) {
            throw new AlreadyPresentException("username");
        }

        if (this.brewerRepository.findByEmail(brewerDto.getEmail()).isPresent()) {
            throw new AlreadyPresentException("email");
        }

        brewerDto.setPassword(bCryptPasswordEncoder.encode(brewerDto.getPassword()));

        Brewer brewer = this.dtoToBrewerConverter.convert(brewerDto);

        if (brewer == null) {
            throw new ConversionException();
        }

        return this.brewerToFatDtoConverter.convert(this.brewerRepository.save(brewer));
    }

    public BrewerFatDto editBrewer(String username, String usernameFromToken, BrewerDto brewerDto) throws BrewerNotFoundException, AccessDeniedException {

        if (!username.equals(usernameFromToken)) {
            throw new AccessDeniedException(ITEM_FROM_EXCEPTION);
        }

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);

        brewer.setEmail(brewerDto.getEmail());
        brewer.setName(brewerDto.getName());
        brewer.setMaxBrew(brewerDto.getMaxBrew());

        return this.brewerToFatDtoConverter.convert(this.brewerRepository.save(brewer));
    }

    @Transactional
    public void deleteBrewer(String username, String usernameFromToken) throws AccessDeniedException, BrewerNotFoundException {
        if (!username.equals(usernameFromToken)) {
            throw new AccessDeniedException(ITEM_FROM_EXCEPTION);
        }

        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);

        this.toolRepository.deleteByBrewer(brewer);
        this.brewRepository.deleteByBrewer(brewer);
        this.recipeIngredientRepository.deleteAllByRecipeIn(this.recipeRepository.findAllByBrewer(brewer));
        this.ingredientRepository.deleteByBrewer(brewer);
        this.recipeRepository.deleteByBrewer(brewer);
        this.brewerRepository.delete(brewer);
    }
}
