package com.ensat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;


    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 64 )
    private String password;

    private int level = 1;
    private int coins = 5000;

    @JsonIgnore
    private Long currentTournamentId = 0L;
    @JsonIgnore
    private Long group_id = 0L;
    @JsonIgnore
    private Boolean attendTournament = false;


}

