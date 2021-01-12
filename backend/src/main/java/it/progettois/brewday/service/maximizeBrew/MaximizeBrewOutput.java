package it.progettois.brewday.service.maximizeBrew;

import it.progettois.brewday.common.dto.RecipeIngredientDto;
import lombok.Data;

import java.util.List;

@Data
public class MaximizeBrewOutput {

    private List<RecipeIngredientDto> ingredients;
    private double FO;
}
