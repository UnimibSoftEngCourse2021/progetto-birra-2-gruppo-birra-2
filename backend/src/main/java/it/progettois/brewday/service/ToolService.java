package it.progettois.brewday.service;


import it.progettois.brewday.common.converter.DtoToToolConverter;
import it.progettois.brewday.persistence.model.Tool;
import it.progettois.brewday.common.converter.ToolToDtoConverter;
import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.persistence.repository.ToolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToolService {

    private final ToolRepository toolRepository;
    private final ToolToDtoConverter toolToDtoConverter;
    private final DtoToToolConverter dtoToToolConverter;

    public ToolService(ToolRepository toolRepository, ToolToDtoConverter toolToDtoConverter, DtoToToolConverter dtoToToolConverter) {
        this.toolRepository = toolRepository;
        this.toolToDtoConverter = toolToDtoConverter;
        this.dtoToToolConverter = dtoToToolConverter;
    }

    public List<ToolDto> getTools() {
        return this.toolRepository.findAll()
                .stream().map(this.toolToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public Tool createTool(ToolDto toolDto) {
        Tool tool = dtoToToolConverter.convert(toolDto);
        return this.toolRepository.save(tool);
    }

    public boolean deleteTool(Integer id) {

        if(this.toolRepository.existsById(id)) {
            this.toolRepository.deleteById(id);
            return true;
        }
        else return false;
    }

    public boolean editTool(Integer id, ToolDto toolDto){

        if(this.toolRepository.existsById(id)){
             toolDto.setToolId(id);
             Tool tool = dtoToToolConverter.convert(toolDto);
             this.toolRepository.save(tool);

        return true;
        }

        else return false;
    }
}



