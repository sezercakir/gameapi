package com.ensat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tournamentgroup")
public class TournamentGroup
{

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @JsonIgnore
    private Long groupId;

    @NotNull private Long playerId;
    @NotNull private String userName;
    @NotNull private Integer score;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "tournament_id",nullable = false, insertable=false, updatable=false)
    private Tournament tournament;
}
