package it.progettois.brewday.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.progettois.brewday.common.dto.RecipeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JsonToRecipeDtoConverter implements Converter<String, RecipeDto> {

    @Override
    public RecipeDto convert(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, RecipeDto.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
