package it.progettois.brewday.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Brew {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "brew_id")
    private Integer brewId;

    private Integer quantity;

    @Column(name = "start_date")
    private Timestamp startDate;

    private String duration;

    private String note;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}