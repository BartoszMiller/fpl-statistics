package com.fplstatistics.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fplstatistics.app.json.DataJson;
import com.fplstatistics.app.json.PlayerJson;
import com.fplstatistics.app.json.TeamJson;
import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.model.Position;
import com.fplstatistics.app.round.RoundScore;
import com.fplstatistics.app.season.Season;
import com.fplstatistics.app.team.Team;
import com.fplstatistics.app.repo.PlayerRepository;
import com.fplstatistics.app.round.RoundScoreRepository;
import com.fplstatistics.app.season.SeasonRepository;
import com.fplstatistics.app.team.TeamRepository;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
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
    RoundScoreRepository roundScoreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSeasons() {

        createNewSeason("2016-17");
        createNewSeason("2017-18");
        createNewSeason("2018-19");
        createNewSeason("2019-20");
        createNewActiveSeason("2020-21");
    }

    @Test
    void initNewSeason() throws IOException {

        createSeasons();

        initTeamsForActiveSeason();
        initPlayersForActiveSeason();
        exportRoundScoresToDatabase("2019-20");
    }

    private void exportRoundScoresToDatabase(String seasonCode) {

        List<String[]> roundScoresCsv = readFromCSV("/merged-gw/" + seasonCode + ".csv");
        Season season = seasonRepository.findByCode(seasonCode);

        roundScoresCsv.forEach(roundScoreCsv -> {

            RoundScore roundScore = new RoundScore();
            roundScore.setSeason(season);
            roundScore.setAssists(Integer.parseInt(roundScoreCsv[1]));
            roundScore.setBonus(Integer.parseInt(roundScoreCsv[2]));
            roundScore.setCleanSheets(Integer.parseInt(roundScoreCsv[4]));
            roundScore.setCreativity(Double.parseDouble(roundScoreCsv[15]));
            roundScore.setGoals(Integer.parseInt(roundScoreCsv[9]));
            roundScore.setIct(Double.parseDouble(roundScoreCsv[10]));
            roundScore.setInfluence(Double.parseDouble(roundScoreCsv[11]));
            roundScore.setKickOff(ZonedDateTime.parse(roundScoreCsv[12]));
            roundScore.setMinutes(Integer.parseInt(roundScoreCsv[13]));
            roundScore.setPoints(Integer.parseInt(roundScoreCsv[25]));
            roundScore.setRound(seasonCode.equals("2019-20") ? postCovidRound(Integer.parseInt(roundScoreCsv[19])) : Integer.parseInt(roundScoreCsv[19]));
            roundScore.setThreat(Double.parseDouble(roundScoreCsv[24]));
            roundScore.setFirstName(roundScoreCsv[0].substring(0, roundScoreCsv[0].indexOf("_")));
            roundScore.setLastName(roundScoreCsv[0].substring(roundScoreCsv[0].indexOf("_") + 1, roundScoreCsv[0].lastIndexOf("_")));
            roundScore.setPlayer(playerRepository.findByFirstNameAndLastName(roundScore.getFirstName(), roundScore.getLastName()));
            roundScore.setSeasonRound(Integer.parseInt(seasonCode.replace("-", "") + String.format("%02d", roundScore.getRound())));
            roundScoreRepository.save(roundScore);
        });
    }

    private int postCovidRound(int roundPostCovid) {
        return roundPostCovid > 38 ? roundPostCovid - 9 : roundPostCovid;
    }

    private void createNewSeason(String years) {
        Season season = new Season();
        season.setCode(years);
        seasonRepository.save(season);
    }

    private void createNewActiveSeason(String years) {
        seasonRepository.resetActiveFlag();
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

        playerRepository.resetCurrentTeam();

        Season activeSeason = seasonRepository.findByActive(true);
        DataJson dataJson = objectMapper.readValue(this.getClass().getResourceAsStream("/api-data/" + activeSeason.getCode() + ".json"), DataJson.class);
        dataJson.setTeamsOnPlayers();

        List<PlayerJson> playersJson = dataJson.getPlayers();
        playersJson.forEach(playerJson -> {

            Player player = playerRepository.findByCode(playerJson.getCode());
            Team currentTeam = teamRepository.findByName(playerJson.getTeamJsonObject().getName());

            if (player == null) {
                Player newPlayer = new Player();
                newPlayer.setCode(playerJson.getCode());
                newPlayer.setWebName(playerJson.getWeb_name());
                newPlayer.setFirstName(playerJson.getFirst_name());
                newPlayer.setLastName(playerJson.getSecond_name());
                newPlayer.setCurrentPrice(((double) playerJson.getNow_cost()) / 10);
                newPlayer.setCurrentTeam(currentTeam);
                newPlayer.setCurrentPosition(Position.getPositionByCode(playerJson.getElement_type()).name());
                playerRepository.save(newPlayer);

            } else {
                player.setCurrentTeam(currentTeam);
                playerRepository.save(player);
            }
        });
    }

    private List<String[]> readFromCSV(String file) {
        URL resourceAsStream = this.getClass().getResource(file);
        List<String[]> lines;
        try (CSVReader reader = new CSVReader(new FileReader(new File(resourceAsStream.toURI())))) {
            lines = reader.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        lines.remove(0);
        return lines;
    }
}
