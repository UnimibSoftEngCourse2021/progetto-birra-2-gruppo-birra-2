package it.progettois.brewday.common.dto;


import it.progettois.brewday.common.constant.ToolUnit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToolDto {

    private Integer toolId;
    private String name;
    private Integer capacity;
    private ToolUnit unit;
    private Integer quantity;
    private String description;
    private String username;
}
