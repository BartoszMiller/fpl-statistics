package com.fplstatistics.app.repo;


import com.fplstatistics.app.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    Season getSeasonByCode(String code);
}
