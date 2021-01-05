package it.progettois.brewday.common.converter;


import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.persistence.model.Tool;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToToolConverter implements Converter<ToolDto, Tool> {

    @Override
    public Tool convert(ToolDto toolDto) {
        Tool tool = new Tool();

        if (toolDto.getToolId() != null)
            tool.setToolId(toolDto.getToolId());
            tool.setName(toolDto.getName());
            tool.setCapacity(toolDto.getCapacity());
            tool.setQuantity(toolDto.getQuantity());
            tool.setDescription(toolDto.getDescription());


        return tool;

    }

}
