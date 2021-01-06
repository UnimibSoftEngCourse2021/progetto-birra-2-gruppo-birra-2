package it.progettois.brewday.common.dto;

import it.progettois.brewday.common.constant.IngredientUnit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToolDto {

    private Integer toolId;
    private String name;
    private Integer capacity;
    private IngredientUnit unit;
    private Integer quantity;
    private String description;
}
