package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.persistence.model.Brewer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DtoToBrewerConverter implements Converter<BrewerDto, Brewer> {

    @Override
    public Brewer convert(BrewerDto brewerDto) {

        Brewer brewer = new Brewer();

        brewer.setUsername(brewerDto.getUsername());
        brewer.setPassword(brewerDto.getPassword());
        brewer.setName(brewerDto.getName());
        brewer.setEmail(brewerDto.getEmail());

        brewer.setStorage(new ArrayList<>());
        brewer.setIngredients(new ArrayList<>());
        brewer.setTools(new ArrayList<>());
        brewer.setRecipes(new ArrayList<>());


        return brewer;
    }
}
