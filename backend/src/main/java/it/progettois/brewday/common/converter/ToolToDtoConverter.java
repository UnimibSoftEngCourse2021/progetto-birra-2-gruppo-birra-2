package it.progettois.brewday.common.converter;

import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.persistence.model.Tool;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToolToDtoConverter implements Converter<Tool, ToolDto> {

    @Override
    public ToolDto convert(Tool tool) {
        return ToolDto.builder()
                .capacity(tool.getCapacity())
                .description(tool.getDescription())
                .name(tool.getName())
                .quantity(tool.getQuantity())
                .toolId(tool.getToolId())
                .build();
    }
}
