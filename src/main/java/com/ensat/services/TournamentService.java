package com.ensat.services;

import com.ensat.dto.UserDTO;
import com.ensat.entities.Tournament;
import com.ensat.entities.TournamentGroup;
import com.ensat.entities.TournamentPlayers;
import com.ensat.entities.User;
import com.ensat.enums.Enums;
import com.ensat.repositories.ITournamentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;


@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentService
{
    @Autowired
    private ITournamentRepository iTournamentRepository;

    private TournamentPlayersService tournamentPlayersService;

    private UserService userService;

    private TournamentGroupService tournamentGroupService;

    private Long ongoingTournament_id;

    private Enums.TournamentStatus tournamentStatus = Enums.TournamentStatus.UNAVAILABLE;

    /**
     * Initialize the first tournament and creates automatically based on UTC timezone.
     * Tournament starts at 00.00 and ends at 20.00 every day.
     * When the tournaments end, it updated all status and flags.
     * <p>
     * Scheduled(cron = "0 0 0-20 * * *", zone = "UTC") means that tournament() function
     * runs 0th and 20th hour of every day.
     */
    @Scheduled(cron = "0 24 3,20 * * *", zone = "UTC")
    public void tournament()
    {
        // create tournament periodically
        if(LocalDateTime.now(ZoneId.of("UTC")).getHour() == 0 &&
                LocalDateTime.now(ZoneId.of("UTC")).getMinute() == 0 &&
                tournamentStatus == Enums.TournamentStatus.TERMINATED)
        {
            tournamentStatus = Enums.TournamentStatus.ONGOING;
            Tournament tournament = new Tournament();
            iTournamentRepository.save(tournament);
            ongoingTournament_id = iTournamentRepository.findFirstByOrderByIdDesc().getId();
        }
        // create first tournament in row match game
        else if (tournamentStatus == Enums.TournamentStatus.UNAVAILABLE && (
                LocalDateTime.now(ZoneId.of("UTC")).isAfter(LocalDate.now().atStartOfDay()) ||
                        LocalDateTime.now(ZoneId.of("UTC")).isEqual(LocalDate.now().atStartOfDay()) ))
        {
            tournamentStatus = Enums.TournamentStatus.ONGOING;
            Tournament tournament = new Tournament();
            iTournamentRepository.save(tournament);
            ongoingTournament_id = iTournamentRepository.findFirstByOrderByIdDesc().getId();
        }
        // terminate the ongoing tournament
        if (LocalDateTime.now(ZoneId.of("UTC")).getHour() == 20 &&
                LocalDateTime.now(ZoneId.of("UTC")).getMinute() == 0 &&
                tournamentStatus == Enums.TournamentStatus.ONGOING)
        {
            // terminating the current tournament
            Tournament tournament = iTournamentRepository.findById(ongoingTournament_id).get();
            tournament.setOngoing(false);
            iTournamentRepository.save(tournament);
            tournamentStatus = Enums.TournamentStatus.TERMINATED;
            // update all users attendTournament value to false
            userService.updateAttendTournament2False();
            // update all top 10 of group's player claim status to true
            tournamentGroupService.updateTop10ScoresForTournament(ongoingTournament_id);
        }
    }

    /**
     * After the tournament award-winning players are updated coins amount.
     *
     * @param tournamentId id of the tournament
     * @param playerId id of the player
     * @param coins amount of rewarded coins
     * @return UserDTO Response-body, it has updated level information.
     */
    public UserDTO updateClaim(Long tournamentId,  Long playerId, int coins) {
        tournamentPlayersService.updateClaim(tournamentId, playerId);

        return userService.updateClaim(playerId, coins);
    }

    /**
     * Checks the tournament is active or not.
     *
     * @param tournamentId id of a tournament that will be searched
     * @return boolean, whether the tournament is online or not.
     */
    public boolean checkOngoingStatus(Long tournamentId) {  return iTournamentRepository.findOngoingById(tournamentId);}

    /**
     * It updates the user's tournament related information when the player entered the tournament.
     * Changes the AttendTournament(true), Group_id(groupNumber) and
     * CurrentTournamentId(tournamentId).
     *
     * @param tournamentId id of tournament that the player will be added.
     * @param playerId if of player.
     * @param groupNumber id of the group.
     */
    public void userEntranceTournament(Long tournamentId, Long playerId, Long groupNumber)
    { userService.userEntranceTournament(tournamentId, playerId, groupNumber);}

    /**
     * It controls the group size.
     *
     * @param groupNumber long value, id of group of current tournament.
     *
     * @return boolean
     */
    public boolean checkGroupSize(Long groupNumber) {
        return tournamentGroupService.checkGroupSize(ongoingTournament_id, groupNumber);
    }

    /**
     * Based on groupId it gets the top ten player of
     * the tournament group.
     *
     * @param groupId id of target group
     * @return List of TournamentGroup,
     */
    public List<TournamentGroup> getGroupLeaderBoard(Long groupId)
    {
        return tournamentGroupService.getGroupLeaderBoard(ongoingTournament_id, groupId);
    }

    /**
     * It controls the group size.
     *
     * @param groupId long value, id of group of current tournament.
     * @param tournamentId long value, id of the tournament.
     * @param playerId long value, id of the player.
     *
     * @return int, rank of the player.
     */
    public int getPlayerRank(Long tournamentId, Long groupId, Long playerId)
    {
        return tournamentGroupService.getPlayerRank(tournamentId, groupId, playerId);
    }

    /**
     * @param playerId player id for searching the tournament's players database for
     *                 checking to claim reward status.
     * @return Enums.TournamentPlayerStatus
     */
    public Enums.TournamentPlayerStatus searchTournamentPlayersClaim(Long playerId)
    {
        if (userService.getUserById(playerId).get().getCurrentTournamentId() < ongoingTournament_id -1)
        {
            // player is nor responsible for more than one previous claim reward status.
            return Enums.TournamentPlayerStatus.CLAIMED;
        }
        // player participated in the previous tournament.
        if (tournamentPlayersService.searchClaim(ongoingTournament_id-1L, playerId))
        {
            // player did not claim the own reward. it cannot be added current tournament.
            return Enums.TournamentPlayerStatus.NOT_CLAIMED;
        }
        else
        {
            // player may not have participated in the previous tournament.
            return Enums.TournamentPlayerStatus.CLAIMED;
        }
    }

    /**
     * @param user player that is currently added to tournament.
     * @param group_number player's group number according to its level.
     *
     * @return List, returns the current leaderboard of the group after
     * inserting the player to group.
     */
    public List<TournamentGroup> addPlayer2TournamentGroup(User user, Long group_number)
    {

        Tournament tournament = iTournamentRepository.findById(ongoingTournament_id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + ongoingTournament_id));

        TournamentGroup tournamentGroup = new TournamentGroup();
        tournamentGroup.setGroupId(group_number);
        tournamentGroup.setPlayerId(user.getId());
        tournamentGroup.setUserName(user.getUsername());
        tournamentGroup.setScore(0);
        tournamentGroup.setTournament(tournament);

        tournamentGroupService.addPlayer2TournamentGroups(tournamentGroup);
        return getGroupLeaderBoard(ongoingTournament_id);
    }

    /**
     * It just adds the player to players database of tournament.
     *
     * @param player_id player that is currently added to tournament.
     * @param group_number player's group number according to its level.
     *
     */
    public void addPlayer2TournamentPlayers(Long player_id, Long group_number)
    {
        Tournament tournament = iTournamentRepository.findById(ongoingTournament_id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament not found with id: " + ongoingTournament_id));

        TournamentPlayers tournamentPlayers = new TournamentPlayers();
        tournamentPlayers.setPlayerId(player_id);
        tournamentPlayers.setGroupId(group_number);
        tournamentPlayers.setClaimStatus(false);
        tournamentPlayers.setTournament(tournament);

        tournamentPlayersService.addPlayer2PlayersDB(tournamentPlayers);
    }

    public Long searchGroupIdForPlayers(Long tournamentId, Long playerId) {return tournamentPlayersService.searchGroupIdForPlayers(tournamentId, playerId);}

    public Optional<User> findUserById(Long id)
    {
        return userService.getUserById(id);
    }

    public boolean isBetween(int rank, int lower, int upper) {return lower <= rank && rank <= upper;}
}
