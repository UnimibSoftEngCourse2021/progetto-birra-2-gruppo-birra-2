package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class RecipeDto {

    private Integer recipeId;
    private Integer version;
    private String name;
    private String description;
    private Integer brewerId;
    private Boolean shared;
    private List<RecipeIngredientDto> ingredients;

}
