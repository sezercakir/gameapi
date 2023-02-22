package com.ensat.controller;
import com.ensat.dto.UserDTO;
import com.ensat.entities.TournamentGroup;
import com.ensat.entities.User;
import com.ensat.enums.Enums;
import com.ensat.exception.Exceptions;
import com.ensat.services.TournamentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/")
public class TournamentController
{
    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/getrank/{tournament_id}/{player_id}")
    public UserDTO.UserRank getRank(@PathVariable Long tournament_id, @PathVariable Long player_id)
    {
        Long group_id = tournamentService.searchGroupIdForPlayers(tournament_id, player_id);
        int rank = tournamentService.getPlayerRank(tournament_id, group_id, player_id);
        return new UserDTO.UserRank(rank, player_id);
    }

    @GetMapping("/getleaderboard/{player_id}")
    @ResponseBody
    public List<TournamentGroup> getLeaderBoard(@PathVariable Long player_id)
    {

        Long group_id = tournamentService.searchGroupIdForPlayers(tournamentService.getOngoingTournament_id(), player_id);

        return tournamentService.getGroupLeaderBoard(group_id);
    }


    @PutMapping("/claimreward/{tournament_id}/{player_id}")
    @ResponseBody
    public UserDTO claimReward(@PathVariable Long tournament_id, @PathVariable Long player_id )
    {
        Long group_id = tournamentService.searchGroupIdForPlayers(tournamentService.getOngoingTournament_id(), player_id);
        int rank = tournamentService.getPlayerRank(tournament_id, group_id, player_id);

        // just terminated rewards of tournaments can be claimed
        if (tournamentService.checkOngoingStatus(tournament_id))
            throw new Exceptions.ClaimOngoingTournament("Rewards of ongoing tournaments cannot be claimed.");

        if (rank == 1)
        {
            return tournamentService.updateClaim(tournament_id, player_id, 10000);
        }
        else if (rank == 2)
        {
            return tournamentService.updateClaim(tournament_id, player_id, 5000);
        } 
        else if (rank == 3) 
        {
            return tournamentService.updateClaim(tournament_id, player_id, 3000);
        }
        else if (tournamentService.isBetween(rank, 4, 10))
        {
            return tournamentService.updateClaim(tournament_id, player_id, 1000);
        }
        else
        {
            throw new Exceptions.ClaimRewardWrongPlayer("The player cannot be claim a reward from tournament.");
        }
    }


    @PostMapping("/entertournament/{player_id}")
    @ResponseBody
    public List<TournamentGroup> enterTournament(@PathVariable Long player_id)
    {
        /* Checking Some Preconditions */
        if (tournamentService.getTournamentStatus() == Enums.TournamentStatus.UNAVAILABLE ||
                tournamentService.getTournamentStatus() == Enums.TournamentStatus.TERMINATED)
        {
            throw new Exceptions.TournamentNotFound("Active tournament is not found.");
        }
        // prevent to add non-existed user to tournament
        Optional<User> user_opt = tournamentService.findUserById(player_id);
        if (!user_opt.isPresent())
        {
            throw new Exceptions.UserNotFound("User not found with given player id.");
        }
        // prevent already tournament player adding again..
        if (user_opt.get().getAttendTournament() && Objects.equals(user_opt.get().getCurrentTournamentId(), tournamentService.getOngoingTournament_id()))
        {
            throw new Exceptions.PlayerAlreadyAttendTournament("Player already attend the tournament with given id.");
        }
        // prevent add tournament player if the player didn't claim the own reward in previous tournament.
        if (tournamentService.searchTournamentPlayersClaim(player_id) == Enums.TournamentPlayerStatus.NOT_CLAIMED)
        {
            throw new Exceptions.PlayerClaimError();
        }
        // check the user level for entering the tournament.
        if (user_opt.get().getLevel() < 20)
        {
            throw new Exceptions.MissingLevel("Level is not sufficient to enter the new tournament.");
        }
        // calculation the player's group level
        User user = user_opt.get();
        int player_level = user.getLevel();
        Long group_number = (long) (player_level % 100 + 1);

        // checking group size whether over 20 or not.
        if (tournamentService.checkGroupSize(group_number))
        {
            throw new Exceptions.GroupSizeOver("Group size is more than 20 with given group id.");
        }
        // update player information related with entering a new tournament
        tournamentService.userEntranceTournament(tournamentService.getOngoingTournament_id(), player_id, group_number);
        // add player to tournament's players database
        tournamentService.addPlayer2TournamentPlayers(player_id, group_number);
        // assign player the group and return the current leaderboard with same group layers
        return tournamentService.addPlayer2TournamentGroup(user,group_number);
    }
}
