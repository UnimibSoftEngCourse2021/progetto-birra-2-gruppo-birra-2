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
        brewerDto.setUsername(brewer.getUsername());
        brewerDto.setName(brewer.getName());
        brewerDto.setEmail(brewer.getEmail());
        brewerDto.setMaxBrew(brewer.getMaxBrew());
        return brewerDto;
    }
}
