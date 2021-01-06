package it.progettois.brewday.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recipe_id")
    private Integer recipeId;

    private Integer version;

    private String name;

    private String description;

    private Boolean shared;

    @ManyToOne
    @JoinColumn(name = "brewer_id")
    private Brewer brewer;

    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredient> ingredients;

}
