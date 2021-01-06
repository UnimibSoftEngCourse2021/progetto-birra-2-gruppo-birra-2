package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.persistence.model.Brewer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BrewerToDtoConverter implements Converter<Brewer, BrewerDto> {

    @Override
    public BrewerDto convert(Brewer brewer) {

        BrewerDto brewerDto = new BrewerDto();
        brewerDto.setEmail(brewer.getEmail());
        brewerDto.setName(brewer.getName());
        brewerDto.setUsername(brewer.getUsername());

        return brewerDto;
    }
}
