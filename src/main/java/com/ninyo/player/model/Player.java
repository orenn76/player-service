package com.ninyo.player.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "player")
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String playerID;
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private String birthCountry;
    private String birthState;
    private String birthCity;
    private Integer deathYear;
    private Integer deathMonth;
    private Integer deathDay;
    private String deathCountry;
    private String deathState;
    private String deathCity;
    private String nameFirst;
    private String nameLast;
    private String nameGiven;
    private Integer weight;
    private Integer height;
    private String bats;
    private String throwz;
    private LocalDate debut;
    private LocalDate finalGame;
    private String retroID;
    private String bbrefID;

    @Transient
    private String debutStr;
    @Transient
    private String finalGameStr;
}
