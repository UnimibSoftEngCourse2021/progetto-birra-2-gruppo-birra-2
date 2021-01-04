package it.progettois.brewday.common.dto;

import it.progettois.brewday.common.constant.IngredientType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientDto {

    private Integer ingredientId;
    private String name;
    private String description;
    private String unit;
    private IngredientType type;

}
