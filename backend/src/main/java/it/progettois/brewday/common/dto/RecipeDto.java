package it.progettois.brewday.common.dto;

import lombok.Data;

import java.util.List;


@Data
public class RecipeDto {

    private Integer recipeId;
    private Integer version;
    private String name;
    private String description;
    private String username;
    private Boolean shared;
    private List<RecipeIngredientDto> ingredients;

}
