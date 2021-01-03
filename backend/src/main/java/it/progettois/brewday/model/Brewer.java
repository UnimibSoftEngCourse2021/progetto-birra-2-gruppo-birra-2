package it.progettois.brewday.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Brewer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer brewerId;

    @Column(updatable = false, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "toolId")
    private List<Tool> tools;

    @OneToMany(mappedBy = "recipeId")
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "brewerIngredientId")
    private List<BrewerIngredient> storage;
}
