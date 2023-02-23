package com.ensat.repositories;

import com.ensat.entities.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITournamentGroupRepository extends JpaRepository<TournamentGroup, Long> {
    TournamentGroup findByTournamentIdAndPlayerId(Long tournamentId, Long playerId);
    Long countByTournamentIdAndGroupId(Long tournamentId, Long groupId);
    List<Long> findDistinctGroupIdByTournamentId(Long tournamentId);
    int countByTournamentIdAndGroupIdAndScoreGreaterThan(Long tournamentId, Long groupId, Integer score);
    int findScoreByTournamentIdAndGroupIdAndPlayerId(Long tournamentId, Long groupId, Long playerId);
    @Query("SELECT tg.playerId, tg.score, tg.userName FROM TournamentGroup tg " +
            "WHERE tg.tournament.id = :tournamentId AND tg.groupId = :groupId " +
            "ORDER BY tg.score DESC")
    List<TournamentGroup> findPlayerIdAndScoreAndUserNameByTournamentIdAndGroupIdAndSortedByScore(
            @Param("tournamentId") Long tournamentId,
            @Param("groupId") Long groupId);
    List<Long> findTop10PlayerIdByTournamentIdAndGroupIdOrderByScoreDesc(Long tournamentId, Long groupId);
}
