package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BrewerFatDto {
    private String username;
    private String name;
    private String email;
    private Integer max_brew;
    private List<ToolDto> tools;
    private List<RecipeDto> recipes;
    private List<BrewerIngredientDto> storage;
    private List<IngredientDto> ingredients;
}


