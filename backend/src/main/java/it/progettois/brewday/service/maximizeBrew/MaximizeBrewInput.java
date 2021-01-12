package it.progettois.brewday.service.maximizeBrew;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MaximizeBrewInput {
    private List<Integer> ingredients;
    private List<Integer> storage;
    private List<Double> proportions;
    private int capacity;
}
