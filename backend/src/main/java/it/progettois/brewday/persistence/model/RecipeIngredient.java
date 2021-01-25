package it.progettois.brewday.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "recipe_ingredient")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recipe_ingredient_id")
    private Integer recipeIngredientId;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    //absolute value
    private Double quantity;

    // This value is the sum of all the recipe ingredient's relative quantities and is used to convert
    // back to relative quantity
    private Double originalTotQuantity;
}