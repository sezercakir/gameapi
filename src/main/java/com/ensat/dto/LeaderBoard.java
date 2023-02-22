package com.ensat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeaderBoard
{
    private List<UserDTO.UserScoreDTO> leaderBoard;
}
