package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BrewTodayDto {

    private String recipeName;
    private List<RecipeIngredientDto> ingredientQuantities;

}
