package it.progettois.brewday.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BrewerIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "brewer_ingredient_id")
    private Integer brewerIngredientId;

    @ManyToOne
    @JoinColumn(name = "brewer_id")
    private Brewer brewer;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private Integer quantity;
}
