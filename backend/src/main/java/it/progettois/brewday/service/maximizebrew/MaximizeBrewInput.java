package it.progettois.brewday.service.maximizebrew;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MaximizeBrewInput {
    private List<String> ingredientNames;
    private List<Double> storage;
    private List<Double> proportions;
    private int capacity;
}
