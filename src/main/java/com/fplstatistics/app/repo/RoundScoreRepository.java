package com.fplstatistics.app.repo;

import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.model.RoundScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoundScoreRepository extends JpaRepository<RoundScore, Integer> {

    @Modifying
    @Query("update RoundScore rs set rs.round = ?2 where rs.round = ?1")
    @Transactional
    void updateRound(int before, int after);

    List<RoundScore> findBySeasonRoundBetweenAndPlayerIn(int from, int to, Collection<Player> players);
}
