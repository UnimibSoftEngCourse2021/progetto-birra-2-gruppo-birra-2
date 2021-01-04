package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToolDto {

    private Integer toolId;
    private String name;
    private Integer capacity;
    private Integer quantity;
    private String description;
}
