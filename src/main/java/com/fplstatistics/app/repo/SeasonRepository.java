package com.fplstatistics.app.repo;


import com.fplstatistics.app.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    @Modifying
    @Query("update Season s set s.active = false")
    @Transactional
    void resetActiveFlag();

    Season findByCode(String code);

    Season findByActive(boolean active);
}
