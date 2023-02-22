package com.ensat;

import com.ensat.exception.Exceptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.server.ResponseStatusException;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
public class SpringBootWebApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringBootWebApplication.class, args);
        }
        catch (Exceptions.TournamentNotFound | Exceptions.UserNotFound |
               Exceptions.ResourceNotFound e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, null, e);
        }
        catch (Exceptions.PlayerAlreadyAttendTournament | Exceptions.GroupSizeOver | Exceptions.PlayerClaimError e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null, e);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "500", e);
        }

    }

}
