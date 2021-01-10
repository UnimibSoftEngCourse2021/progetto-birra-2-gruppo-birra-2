package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class BrewDto {

    private String username;
    private Integer quantity;
    private Timestamp startDate;
    private String duration;
    private String note;
    private RecipeDto recipe;
    private String brewerUsername;
}
