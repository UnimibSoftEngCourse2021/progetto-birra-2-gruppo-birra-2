package it.progettois.brewday.common.dto;

import lombok.Data;

@Data
public class RecipeIngredientDto {

    private Integer recipeIngredientId;
    private Integer ingredientId;
    private String ingredientName;
    private Integer quantity;
}
