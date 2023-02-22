package com.ensat.services;

import com.ensat.entities.TournamentPlayers;
import com.ensat.repositories.ITournamentPlayersRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
public class TournamentPlayersService
{
    @Autowired
    private ITournamentPlayersRepository iTournamentPlayersRepository;

    /**
     * Function returns boolean value, true represents the player didn't claim
     * own reward at the previous tournament.
     *
     * @param tournamentId long value, id of PREVIOUS tournament.
     * @param playerId long value, id of player of current tournament.
     *
     * @return boolean, whether the player claim reward.
     */
    public boolean searchClaim(Long tournamentId, Long playerId)
    {
        return iTournamentPlayersRepository.findByTournamentIdAndPlayerIdAndClaimStatus(tournamentId, playerId, true);
    }

    /**
     * Get group id with using tournament id and player id.
     *
     * @param tournamentId long value, id of tournament.
     * @param playerId long value, id of player of current tournament.
     * @return Long, group id found
     */
    public Long searchGroupIdForPlayers(Long tournamentId, Long playerId)
    {
        return iTournamentPlayersRepository.findGroupIdByTournamentIdAndPlayerId(tournamentId, playerId);
    }

    /**
     * Update the player claim status after the getting the
     * own reward. It's just called by rewarded players.
     *
     * @param tournamentId long value, id of CURRENT tournament.
     * @param playerId long value, id of player of current tournament.
     */
    public void updateClaim(Long tournamentId, Long playerId)
    {
        TournamentPlayers tournamentPlayer = iTournamentPlayersRepository.findByTournamentIdAndPlayerId
                        (tournamentId, playerId);

        tournamentPlayer.setClaimStatus(false);
        iTournamentPlayersRepository.save(tournamentPlayer);
    }

    /**
     * Update the player claim status at the end of the tournament.
     * It changes the claim status of rewarded players to true.
     *
     * @param ongoingTournamentId long value, id of tournament.
     * @param playerIds List<Long> value, ids of player of current tournament.
     */
    public void updateClaimEndTournament(Long ongoingTournamentId, List<Long> playerIds)
    {

        List<TournamentPlayers> tournamentPlayers = iTournamentPlayersRepository.
                                                    findByTournamentIdAndPlayerIds(ongoingTournamentId, playerIds);

        for (TournamentPlayers player : tournamentPlayers)
            player.setClaimStatus(true);

        iTournamentPlayersRepository.saveAll(tournamentPlayers);
    }

    public void addPlayer2PlayersDB(TournamentPlayers tournamentPlayers) { iTournamentPlayersRepository.save(tournamentPlayers);}
}
