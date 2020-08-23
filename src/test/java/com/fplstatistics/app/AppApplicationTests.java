package com.fplstatistics.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fplstatistics.app.json.DataJson;
import com.fplstatistics.app.json.TeamJson;
import com.fplstatistics.app.model.Season;
import com.fplstatistics.app.model.Team;
import com.fplstatistics.app.repo.SeasonRepository;
import com.fplstatistics.app.repo.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void initSeason_2020_2021() {

        // init season
        Season season = new Season();
        season.setCode("2020_2021");
        seasonRepository.save(season);
    }

    @Test
    void initTeams_2020_2021() throws IOException {

        DataJson dataJson = objectMapper.readValue(this.getClass().getResourceAsStream("/api-data/2020_2021.json"), DataJson.class);
        List<TeamJson> teamsJson = dataJson.getTeams();

        List<Team> teams = new ArrayList<>();
        teamsJson.forEach(teamJson -> {
            Team team = new Team();
            team.setName(teamJson.getName());
            team.setShortName(teamJson.getShort_name());
            teams.add(teamRepository.save(team));
        });

        Season season = seasonRepository.getSeasonByCode("2020_2021");
        season.setTeams(teams);
        seasonRepository.save(season);
    }
}
