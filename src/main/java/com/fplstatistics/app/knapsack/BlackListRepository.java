package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Integer> {

    Optional<BlackList> findByPlayer(Player player);
}
