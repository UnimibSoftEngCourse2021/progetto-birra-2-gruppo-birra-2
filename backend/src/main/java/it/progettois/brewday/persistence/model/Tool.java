package it.progettois.brewday.persistence.model;

import it.progettois.brewday.common.constant.IngredientUnit;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tool_id")
    private Integer toolId;

    private String name;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private IngredientUnit unit;

    private Integer quantity;

    private String description;

    @ManyToOne
    @JoinColumn(name = "brewer_id")
    private Brewer brewer;

}
