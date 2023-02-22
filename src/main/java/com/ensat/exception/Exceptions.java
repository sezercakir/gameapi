package com.ensat.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class Exceptions
{
    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFound extends RuntimeException {

        /**
         * Instantiates a new resource not found exception.
         *
         * @param message the message
         */
        public ResourceNotFound(String message) {
            super(message);
        }
    }
    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class TournamentNotFound extends RuntimeException{
        /**
         * Instantiates a new TournamentNotFound exception.
         *
         * @param message the message
         */
        public TournamentNotFound(String message) {
            super(message);
        }
    }
    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class UserNotFound extends RuntimeException{
        /**
         * Instantiates a new UserNotFound exception.
         *
         * @param message the message
         */
        public UserNotFound(String message) {
            super(message);
        }
    }
    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class GroupNotFound extends RuntimeException{
        /**
         * Instantiates a new GroupNotFound exception.
         *
         * @param message the message
         */
        public GroupNotFound(String message) {
            super(message);
        }
    }
    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class PlayerAlreadyAttendTournament extends RuntimeException{
        /**
         * Instantiates a new PlayerAlreadyAttendTournament exception.
         *
         * @param message the message
         */
        public PlayerAlreadyAttendTournament(String message) {
            super(message);
        }
    }
    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class PlayerClaimError extends RuntimeException{
        /**
         * Instantiates a new PlayerClaimError exception.
         *
         * @param message the message
         */
        public PlayerClaimError(String message) {
            super(message);
        }
    }
    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class GroupSizeOver extends RuntimeException{
        /**
         * Instantiates a new GroupSizeOver exception.
         *
         * @param message the message
         */
        public GroupSizeOver(String message) {
            super(message);
        }
    }

    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class ClaimOngoingTournament extends RuntimeException{
        /**
         * Instantiates a new ClaimOngoingTournament exception.
         *
         * @param message the message
         */
        public ClaimOngoingTournament(String message) {
            super(message);
        }
    }

    @NoArgsConstructor
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class ClaimRewardWrongPlayer extends RuntimeException{
        /**
         * Instantiates a new ClaimOngoingTournament exception.
         *
         * @param message the message
         */
        public ClaimRewardWrongPlayer(String message) {
            super(message);
        }
    }

}




