package it.progettois.brewday.service;

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

    public ToolService(ToolRepository toolRepository, ToolToDtoConverter toolToDtoConverter) {
        this.toolRepository = toolRepository;
        this.toolToDtoConverter = toolToDtoConverter;
    }

    public List<ToolDto> getTools() {
        return this.toolRepository.findAll()
                .stream().map(this.toolToDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
