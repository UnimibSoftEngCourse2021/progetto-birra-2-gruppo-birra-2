package it.progettois.brewday.service;


import it.progettois.brewday.common.converter.DtoToToolConverter;
import it.progettois.brewday.common.converter.ToolToDtoConverter;
import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ToolNotFoundException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.model.Tool;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import it.progettois.brewday.persistence.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ToolService {

    private final ToolRepository toolRepository;
    private final BrewerRepository brewerRepository;
    private final ToolToDtoConverter toolToDtoConverter;
    private final DtoToToolConverter dtoToToolConverter;

    private static final String ITEM_FOR_EXCEPTION = "tool";

    @Autowired
    public ToolService(ToolRepository toolRepository, BrewerRepository brewerRepository, ToolToDtoConverter toolToDtoConverter, DtoToToolConverter dtoToToolConverter) {
        this.toolRepository = toolRepository;
        this.brewerRepository = brewerRepository;
        this.toolToDtoConverter = toolToDtoConverter;
        this.dtoToToolConverter = dtoToToolConverter;
    }

    private Boolean brewerOwnsTool(String username, Integer id) throws BrewerNotFoundException, ToolNotFoundException {
        Brewer brewer = this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new);
        return this.toolRepository.findById(id).orElseThrow(ToolNotFoundException::new).getBrewer().equals(brewer);
    }

    public List<ToolDto> getTools(String username) throws BrewerNotFoundException {

        return this.toolRepository
                .findToolsByBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new))
                .stream()
                .map(toolToDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public ToolDto getTool(String username, Integer id) throws BrewerNotFoundException, AccessDeniedException, ToolNotFoundException {
        if (Boolean.TRUE.equals(brewerOwnsTool(username, id))) {

            Tool tool = toolRepository.findById(id).orElseThrow(ToolNotFoundException::new);
            return this.toolToDtoConverter.convert(tool);

        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);
    }

    public ToolDto createTool(ToolDto toolDto, String username) throws BrewerNotFoundException {

        Tool tool = dtoToToolConverter.convert(toolDto);
        Objects.requireNonNull(tool).setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
        this.toolRepository.save(tool);
        return this.toolToDtoConverter.convert(tool);
    }

    public void deleteTool(String username, Integer id) throws AccessDeniedException, ToolNotFoundException, BrewerNotFoundException {

        if (Boolean.TRUE.equals(brewerOwnsTool(username, id))) {
            this.toolRepository.deleteById(id);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);
    }

    public void editTool(String username, Integer id, ToolDto toolDto) throws AccessDeniedException, ToolNotFoundException, BrewerNotFoundException {

        if (Boolean.TRUE.equals(brewerOwnsTool(username, id))) {
            Tool tool = dtoToToolConverter.convert(toolDto);
            Objects.requireNonNull(tool).setBrewer(this.brewerRepository.findByUsername(username).orElseThrow(BrewerNotFoundException::new));
            tool.setToolId(id);
            this.toolRepository.save(tool);
        } else throw new AccessDeniedException(ITEM_FOR_EXCEPTION);
    }
}
