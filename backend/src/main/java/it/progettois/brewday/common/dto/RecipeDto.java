package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RecipeDto {

    public RecipeDto() {
    }

    public RecipeDto(Integer recipeId, String name, String description, String username, Boolean shared, List<RecipeIngredientDto> ingredients) {
        this.recipeId = recipeId;
        this.name = name;
        this.description = description;
        this.username = username;
        this.shared = shared;
        this.ingredients = ingredients;
    }

    private Integer recipeId;
    private String name;
    private String description;
    private String username;
    private Boolean shared;
    private List<RecipeIngredientDto> ingredients;

}
