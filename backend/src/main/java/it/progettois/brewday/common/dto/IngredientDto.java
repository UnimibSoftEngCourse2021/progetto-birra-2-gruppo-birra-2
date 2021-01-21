package it.progettois.brewday.common.dto;

import it.progettois.brewday.common.constant.IngredientType;
import it.progettois.brewday.common.constant.IngredientUnit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientDto {

    public IngredientDto() {
    }

    public IngredientDto(Integer ingredientId, String name, String description, IngredientUnit unit,
                         IngredientType type, Double quantity, String username, Boolean shared) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.description = description;
        this.unit = unit;
        this.type = type;
        this.quantity = quantity;
        this.username = username;
        this.shared = shared;

    }

    private Integer ingredientId;
    private String name;
    private String description;
    private IngredientUnit unit;
    private IngredientType type;
    private Double quantity;
    private String username;
    private Boolean shared;

}
