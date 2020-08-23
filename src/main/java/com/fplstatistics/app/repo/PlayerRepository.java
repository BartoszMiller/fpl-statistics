package com.fplstatistics.app.repo;

import com.fplstatistics.app.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    boolean existsByCode(int code);
}
