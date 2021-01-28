package it.progettois.brewday.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Brew {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer brewId;

    private Integer quantity;

    @Column(name = "start_date")
    private Timestamp startDate;

    private String duration;

    @Column(length = 1500)
    private String note;

    //Json representation of the recipe
    @Column(length = 2000)
    private String recipe;

    @ManyToOne
    @JoinColumn(name = "username")
    private Brewer brewer;
}
