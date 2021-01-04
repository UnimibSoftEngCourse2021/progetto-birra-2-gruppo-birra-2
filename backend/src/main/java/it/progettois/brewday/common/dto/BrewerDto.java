package it.progettois.brewday.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class BrewerDto {

    private Integer brewerId;
    private String username;
    private String name;
    private String email;
    private List<Integer> tools;
    private List<Integer> recipes;
    private List<Integer> ingredients;
}
