package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrewerIngredientDto {

    private IngredientDto ingredient;
    private Integer quantity;
}
