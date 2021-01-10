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
                .toolId(tool.getToolId())
                .name(tool.getName())
                .capacity(tool.getCapacity())
                .unit(tool.getUnit())
                .quantity(tool.getQuantity())
                .description(tool.getDescription())
                .username(tool.getBrewer().getUsername())
                .build();
    }
}
