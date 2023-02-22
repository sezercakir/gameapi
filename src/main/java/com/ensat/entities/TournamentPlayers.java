package com.ensat.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tournament_players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentPlayers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tournament_id")
    private Long tournamentId;

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "claim_status")
    private boolean claimStatus;

    @Column(name = "group_id")
    private Long groupId;

    @OneToOne
    @JoinColumn(name = "tournament_id",nullable = false, insertable=false, updatable=false)
    private Tournament tournament;

    public boolean getClaimStatus() {
        return claimStatus;
    }
}


