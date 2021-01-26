package it.progettois.brewday.common.dto;


import it.progettois.brewday.common.constant.ToolUnit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToolDto {

    public ToolDto(){

    }
    public ToolDto(Integer toolId, String name, Integer capacity, ToolUnit unit, Integer quantity, String Description,
                   String username) {
        this.toolId = toolId;
        this.name = name;
        this.capacity = capacity;
        this.unit = unit;
        this.quantity = quantity;
        this.description = description;
        this.username = username;
    }

    private Integer toolId;
    private String name;
    private Integer capacity;
    private ToolUnit unit;
    private Integer quantity;
    private String description;
    private String username;
}