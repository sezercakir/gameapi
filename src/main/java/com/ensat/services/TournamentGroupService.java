package com.ensat.services;


import com.ensat.entities.TournamentGroup;
import com.ensat.repositories.ITournamentGroupRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
@AllArgsConstructor
public class TournamentGroupService {

    @Autowired
    private ITournamentGroupRepository iTournamentGroupRepository;

    private final TournamentPlayersService tournamentPlayersService;

    /**
     * It controls the group size.
     *
     * @param tournamentId long value, id of tournament.
     * @param groupId long value, id of group of current tournament.
     *
     * @return boolean
     */
    public boolean checkGroupSize(Long tournamentId, Long groupId) {
        return iTournamentGroupRepository.countByTournamentIdAndGroupId(tournamentId, groupId) > 20;
    }

    /**
     * get player rank value from tournament group.
     *
     * @param tournamentId long value, id of tournament.
     * @param groupId long value, id of group of current tournament.
     * @param playerId id of player of group
     *
     * @return int, rank of player
     */
    public int getPlayerRank(Long playerId, Long tournamentId, Long groupId)
    {
        int player_score = iTournamentGroupRepository.findScoreByTournamentIdAndGroupIdAndPlayerId
                                                            (tournamentId, groupId, playerId);

        return iTournamentGroupRepository.countByTournamentIdAndGroupIdAndScoreGreaterThan
                                                            (tournamentId, groupId, player_score) + 1;
    }

    /**
     * It gets the player id, score and username columns of TournamentGroup
     * table to access the leaderboard of that group.
     *
     * @param tournamentId long value, id of tournament.
     * @param groupId long value, id of group of current tournament.
     *
     * @return List, list of tournament group values as sorted by descending order based on score.
     */
    public List<TournamentGroup> getGroupLeaderBoard(Long tournamentId, Long groupId)
    {
        return iTournamentGroupRepository.findPlayerIdAndScoreAndUserNameByTournamentIdAndGroupIdAndSortedByScore
                                                                                    (tournamentId, groupId);
    }

    /**
     * Adds a tournament-group to TournamentGroup table.
     *
     * @param tournamentGroup tournament-group object.
     */
    public void addPlayer2TournamentGroups(TournamentGroup tournamentGroup) {
        iTournamentGroupRepository.save(tournamentGroup);
    }

    /**
     * Increment the player score in tournament group table.
     *
     * @param currentTournamentId long value, id of tournament.
     * @param player_id long value, id of palyer.
     */
    public void updateScoreBoard(Long player_id, Long currentTournamentId)
    {
        TournamentGroup tournamentGroup = iTournamentGroupRepository.findByTournamentIdAndPlayerId
                                                                (currentTournamentId, player_id);
        tournamentGroup.setScore(tournamentGroup.getScore()+1);
        iTournamentGroupRepository.save(tournamentGroup);
    }

    /**
     * Function finds total unique group id for a tournament.
     * Iterates on group ids and finds top ten player per each.
     * Then, claim status updated to true for these players.
     * It means that they were entitled to claim the award.
     *
     * @param ongoingTournamentId   current tournament id
     */
    public void updateTop10ScoresForTournament(Long ongoingTournamentId)
    {
        List<Long> distinctGroupIds = iTournamentGroupRepository.findDistinctGroupIdByTournamentId(ongoingTournamentId);

        for (Long groupId : distinctGroupIds)
        {
            List<Long> playerIds = iTournamentGroupRepository.findTop10PlayerIdByTournamentIdAndGroupIdOrderByScoreDesc
                                                                (ongoingTournamentId, groupId);
            tournamentPlayersService.updateClaimEndTournament(ongoingTournamentId, playerIds);
        }
    }
}
