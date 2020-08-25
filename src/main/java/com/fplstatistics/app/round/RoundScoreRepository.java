package com.fplstatistics.app.round;

import com.fplstatistics.app.player.Player;
import com.fplstatistics.app.season.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoundScoreRepository extends JpaRepository<RoundScore, Integer> {

    @Modifying
    @Query("update RoundScore rs set rs.round = ?2 where rs.round = ?1")
    @Transactional
    void updateRound(int before, int after);

    List<RoundScore> findBySeasonRoundBetweenAndPlayerIn(int from, int to, Collection<Player> players);

    @Query("select max(rs.round) from RoundScore rs where rs.season = ?1")
    Optional<Integer> findMaxRoundBySeason(Season season);
}
