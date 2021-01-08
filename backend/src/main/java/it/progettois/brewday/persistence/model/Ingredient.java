package it.progettois.brewday.persistence.model;

import it.progettois.brewday.common.constant.IngredientType;
import it.progettois.brewday.common.constant.IngredientUnit;
import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ingredient_id")
    private Integer ingredientId;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private IngredientUnit unit;

    private Double quantity = 0.0;

    @Enumerated(EnumType.STRING)
    private IngredientType type;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipe;

    @ManyToOne
    @JoinColumn(name = "brewer_id")
    private Brewer brewer;

    private Boolean shared;

}
