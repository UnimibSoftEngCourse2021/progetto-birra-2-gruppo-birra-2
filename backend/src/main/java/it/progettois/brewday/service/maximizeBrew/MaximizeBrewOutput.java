package it.progettois.brewday.service.maximizeBrew;

import it.progettois.brewday.common.dto.RecipeIngredientDto;
import lombok.Data;

import java.util.List;

@Data
public class MaximizeBrewOutput {

    private List<MaxedRecipeIngredient> ingredients;
    private double FO;
}
