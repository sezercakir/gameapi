package com.ensat.enums;

public class Enums
{
    public enum PlayerStatus{
        ATTEND_TOURNAMENT(0),
        RANKED(1),
        CLAIMRANK(2);

        private final int value;

        PlayerStatus(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }

    public enum TournamentStatus {
        UNAVAILABLE,
        ONGOING,
        TERMINATED,
    }
    public enum TournamentGroupSatus {
        NOTFOUND(0),
        ADDED(1);

        private final int value;

        TournamentGroupSatus(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }

    public enum TournamentPlayerStatus {
        CLAIMED,
        NOT_CLAIMED,
    }

}
