package com.ensat.repositories;

import com.ensat.entities.TournamentPlayers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITournamentPlayersRepository extends JpaRepository<TournamentPlayers, Long> {
    TournamentPlayers findByTournamentIdAndPlayerId(Long tournamentId, Long playerId);
    boolean findByTournamentIdAndPlayerIdAndClaimStatus(Long tournamentId, Long playerId, boolean claimStatus);
    Long findGroupIdByTournamentIdAndPlayerId(Long tournamentId, Long playerId);
    @Query("SELECT e FROM TournamentPlayers e WHERE e.tournament.id = :tournamentId AND e.playerId IN :playerIds")
    List<TournamentPlayers> findByTournamentIdAndPlayerIds(@Param("tournamentId") Long tournamentId, @Param("playerIds") List<Long> playerIds);
}
