package com.ensat.repositories;

import com.ensat.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITournamentRepository extends JpaRepository<Tournament, Long> {
    Tournament findFirstByOrderByIdDesc();
    boolean findOngoingById(Long tournamentId);

}
