package it.progettois.brewday.service;

import it.progettois.brewday.persistence.model.Tool;
import it.progettois.brewday.persistence.repository.ToolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {

    private final ToolRepository toolRepository;

    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public List<Tool> getTools() {
        return this.toolRepository.findAll();
    }
}
