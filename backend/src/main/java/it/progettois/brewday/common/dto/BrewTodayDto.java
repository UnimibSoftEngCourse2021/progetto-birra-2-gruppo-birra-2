package it.progettois.brewday.common.dto;

import it.progettois.brewday.persistence.model.Recipe;
import it.progettois.brewday.service.maximizeBrew.MaxedRecipeIngredient;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BrewTodayDto {

    private Integer recipeId;
    private String recipeName;
    private String recipeDescription;
    private List<MaxedRecipeIngredient> ingredientQuantities;

}
