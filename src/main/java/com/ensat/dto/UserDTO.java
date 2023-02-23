package com.ensat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO
{
    private final int level;
    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserRank
    {
        private int rank;
        private Long id;
    }
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserCreateDTO {
        private Long id;
        private int level;
        private int coin;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserScoreDTO
    {
        private int score;
        private int level;
    }
}
