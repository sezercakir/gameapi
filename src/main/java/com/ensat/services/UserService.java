package com.ensat.services;
import com.ensat.dto.UserDTO;
import com.ensat.entities.User;
import com.ensat.exception.Exceptions;
import com.ensat.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private IUserRepository iUserRepository;

    private TournamentGroupService tournamentGroupService;

    /**
     * @param user new user that will be added
     */
    public void addUser(User user)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        iUserRepository.save(user);
    }

    /**
     *
     * @param id id of the player that will be updated
     * @return updated part of the user as ResponseBody
     */
    public UserDTO.UserScoreDTO update(Long id)
    {
        User user = getUserById(id).orElseThrow(() -> new Exceptions.UserNotFound("User not found"));
        user.setLevel(user.getLevel()+1);
        user.setCoins(user.getCoins()+25);
        iUserRepository.save(user);

        return new UserDTO.UserScoreDTO(user.getCoins(), user.getLevel());
    }

    /**
     *
     * @param playerId id of the player
     * @param coins rewarded coins for top ten player in tournament.
     * @return updated coins of the user as ResponseBody
     */
    public UserDTO updateClaim(Long playerId, int coins)
    {
        User user = getUserById(playerId).orElseThrow(() -> new Exceptions.UserNotFound("User not found"));
        user.setCoins(user.getCoins() + coins);
        return new UserDTO((user.getCoins()));
    }

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
    {
        User user = getUserById(playerId).orElseThrow(() -> new Exceptions.UserNotFound("User not found"));
        user.setAttendTournament(true);
        user.setGroup_id(groupNumber);
        user.setCurrentTournamentId(tournamentId);
        iUserRepository.save(user);
    }

    /**
     *
     * @param player_id id of the player that will be updated level and coins.
     * @param currentTournamentId id of current tournament
     * @param groupId if of the group
     * @return updated part of the user coins and level as ResponseBody.
     */
    public UserDTO.UserScoreDTO updateScoreBoard(Long player_id, Long currentTournamentId, Long groupId)
    {

        User user = getUserById(player_id).orElseThrow(() -> new Exceptions.UserNotFound("User not found"));
        UserDTO.UserScoreDTO userScoreDTO = new UserDTO.UserScoreDTO(user.getCoins()+25, user.getLevel()+1);

        user.setLevel(user.getLevel()+1);
        user.setCoins(user.getCoins()+25);
        tournamentGroupService.updateScoreBoard(currentTournamentId, player_id);
        iUserRepository.save(user);
        return userScoreDTO;
    }

    /**
     * Update the all true column value "attendTournament" to false
     * at the end of the tournaments.
     */
    public void updateAttendTournament2False() {
        List<User> users = iUserRepository.findByAttendTournament(true);
        for (User user : users) {
            user.setAttendTournament(false);
        }
        iUserRepository.saveAll(users);
    }

    public List<User> findAll()
    {
        return iUserRepository.findAll();
    }

    public Optional<User> getUserById(Long id)
    {
        return iUserRepository.findById(id);
    }
}
