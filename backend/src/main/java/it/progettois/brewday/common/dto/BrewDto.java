package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class BrewDto {

    private Integer brewId;
    private Integer quantity;
    private Timestamp startDate;
    private String duration;
    private String note;
    private RecipeDto recipe;
}
