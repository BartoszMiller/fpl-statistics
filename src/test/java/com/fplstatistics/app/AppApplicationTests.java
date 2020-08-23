package com.fplstatistics.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fplstatistics.app.json.DataJson;
import com.fplstatistics.app.json.PlayerJson;
import com.fplstatistics.app.json.TeamJson;
import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.model.Season;
import com.fplstatistics.app.model.Team;
import com.fplstatistics.app.repo.PlayerRepository;
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
    private PlayerRepository playerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void initNewSeason() throws IOException {

        String seasonCode = "2020-2021";
        createNewActiveSeason(seasonCode);
        initTeamsForActiveSeason();
        initPlayersForActiveSeason();
    }

    private void createNewActiveSeason(String years) {
        Season season = new Season();
        season.setCode(years);
        season.setActive(true);
        seasonRepository.save(season);
    }

    private void initTeamsForActiveSeason() throws IOException {

        Season activeSeason = seasonRepository.findByActive(true);
        DataJson dataJson = objectMapper.readValue(this.getClass().getResourceAsStream("/api-data/" + activeSeason.getCode() + ".json"), DataJson.class);
        List<TeamJson> teamsJson = dataJson.getTeams();

        List<Team> teams = new ArrayList<>();
        teamsJson.stream().filter(teamJson -> !teamRepository.existsByName(teamJson.getName())).forEach(teamJson -> {
            Team team = new Team();
            team.setName(teamJson.getName());
            team.setShortName(teamJson.getShort_name());
            teams.add(teamRepository.save(team));
        });

        activeSeason.setTeams(teams);
        seasonRepository.save(activeSeason);
    }

    private void initPlayersForActiveSeason() throws IOException {

        Season activeSeason = seasonRepository.findByActive(true);
        DataJson dataJson = objectMapper.readValue(this.getClass().getResourceAsStream("/api-data/" + activeSeason.getCode() + ".json"), DataJson.class);
        dataJson.setTeamsOnPlayers();

        List<PlayerJson> playersJson = dataJson.getPlayers();
        playersJson.stream().filter(playerJson -> !playerRepository.existsByCode(playerJson.getCode())).forEach(playerJson -> {
            Player player = new Player();
            player.setCode(playerJson.getCode());
            player.setFirstName(playerJson.getFirst_name());
            player.setLastName(playerJson.getSecond_name());
            player.setCurrentTeam(teamRepository.findByName(playerJson.getTeamJsonObject().getName()));
            playerRepository.save(player);
        });
    }
}
