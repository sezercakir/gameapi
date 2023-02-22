package com.ensat.entities;


import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tournament")
public class Tournament {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime created_at = LocalDateTime.now();

    @NotNull
    @Column(name = "ongoing")
    private Boolean ongoing = true;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tournament")
    private TournamentGroup groups;

    @OneToOne(mappedBy = "tournament", cascade = CascadeType.ALL)
    private TournamentPlayers tournamentPlayers;
}


