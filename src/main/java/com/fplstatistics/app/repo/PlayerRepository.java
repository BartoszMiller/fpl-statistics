package com.fplstatistics.app.repo;

import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    @Modifying
    @Query("update Player p set p.currentTeam = null")
    @Transactional
    void resetCurrentTeam();

    Player findByFirstNameAndLastName(String firstName, String lastName);

    Player findByCode(int code);

    List<Player> findByCurrentTeam(Team team);

    boolean existsByCode(int code);
}
