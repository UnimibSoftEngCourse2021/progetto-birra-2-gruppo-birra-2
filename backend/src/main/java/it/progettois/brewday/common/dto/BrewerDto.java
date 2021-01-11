package it.progettois.brewday.common.dto;

import lombok.Data;


@Data
public class BrewerDto {

    private String username;
    private String password;
    private String name;
    private String email;
    private Integer max_brew;
}
