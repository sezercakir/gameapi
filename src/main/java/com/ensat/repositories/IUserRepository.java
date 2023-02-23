package com.ensat.repositories;

import com.ensat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRepository extends JpaRepository<User, Long> {
    List<User> findByAttendTournament(boolean attendTournament);
}
