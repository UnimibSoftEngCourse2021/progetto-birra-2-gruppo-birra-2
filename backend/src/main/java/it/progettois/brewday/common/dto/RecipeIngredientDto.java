package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RecipeIngredientDto {

    public RecipeIngredientDto() {
    }

    public RecipeIngredientDto(Integer recipeIngredientId, Integer ingredientId, String ingredientName, Double quantity) {
        this.recipeIngredientId = recipeIngredientId;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
    }

    private Integer recipeIngredientId;
    private Integer ingredientId;
    private String ingredientName;
    private Double quantity;
}
