package it.progettois.brewday.service.maximizebrew;

import lombok.Data;

import java.util.List;

@Data
public class MaximizeBrewOutput {

    private List<MaxedRecipeIngredient> ingredients;
    private double FO;
}
