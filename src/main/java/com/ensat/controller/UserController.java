package com.ensat.controller;
import com.ensat.dto.UserDTO;
import com.ensat.entities.User;
import com.ensat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers()
    {
        return userService.findAll();
    }

    @PutMapping("updatelevel/{player_id}")
    @ResponseBody
    public UserDTO.UserScoreDTO updateLevel(@PathVariable Long player_id)
    {
        User user = userService.getUserById(player_id).get();
        if(user.getAttendTournament() && user.getCurrentTournamentId() != 0)
            // when the player is active at tournament.
            return userService.updateScoreBoard(player_id, user.getCurrentTournamentId(), user.getGroup_id());
        else
            return userService.update(player_id);
    }

    @PostMapping("users/createuser")
    @ResponseBody
    public UserDTO.UserCreateDTO createUser(@RequestBody User user)
    {
        userService.addUser(user);
        return new UserDTO.UserCreateDTO(user.getId(), user.getLevel(), user.getLevel());
    }
}
