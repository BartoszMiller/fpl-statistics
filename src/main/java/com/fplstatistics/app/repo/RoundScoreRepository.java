package com.fplstatistics.app.repo;

import com.fplstatistics.app.model.RoundScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundScoreRepository extends JpaRepository<RoundScore, Integer> {
}
