package it.progettois.brewday.model;

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

    private Integer quantity;

    private String description;

    @ManyToOne
    @JoinColumn(name = "brewer_id")
    private Brewer brewer;

}
