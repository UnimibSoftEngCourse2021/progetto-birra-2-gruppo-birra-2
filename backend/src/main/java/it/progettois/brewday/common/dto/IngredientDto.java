package it.progettois.brewday.common.dto;

import it.progettois.brewday.common.constant.IngredientType;
import it.progettois.brewday.common.constant.IngredientUnit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientDto {

    private Integer ingredientId;
    private String name;
    private String description;
    private IngredientUnit unit;
    private IngredientType type;
    private Double quantity;

}
