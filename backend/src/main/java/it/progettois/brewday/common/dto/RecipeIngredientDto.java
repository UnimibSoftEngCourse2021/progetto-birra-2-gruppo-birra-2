package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RecipeIngredientDto {

    public RecipeIngredientDto() {
    }

    public RecipeIngredientDto(Integer recipeIngredientId, Integer ingredientId, String ingredientName, Double quantity, Double originalTotQuantity) {
        this.recipeIngredientId = recipeIngredientId;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.originalTotQuantity = originalTotQuantity;
    }

    private Integer recipeIngredientId;
    private Integer ingredientId;
    private String ingredientName;
    private Double quantity;
    private Double originalTotQuantity;
}
