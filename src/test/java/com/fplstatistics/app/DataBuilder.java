package com.fplstatistics.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fplstatistics.app.json.DataJson;
import com.fplstatistics.app.json.PlayerJson;
import com.fplstatistics.app.json.TeamJson;
import com.fplstatistics.app.player.Player;
import com.fplstatistics.app.player.PlayerRepository;
import com.fplstatistics.app.position.Position;
import com.fplstatistics.app.round.RoundScore;
import com.fplstatistics.app.round.RoundScoreRepository;
import com.fplstatistics.app.season.Season;
import com.fplstatistics.app.season.SeasonRepository;
import com.fplstatistics.app.team.Team;
import com.fplstatistics.app.team.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DataBuilder {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RoundScoreRepository roundScoreRepository;

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
    void initNewSeason() throws IOException, URISyntaxException {

        createSeasons();

        initTeamsForActiveSeason();
        initPlayersForActiveSeason();
        exportRoundScoresToDatabase("2016-17");
        exportRoundScoresToDatabase("2017-18");
        exportRoundScoresToDatabase("2018-19");
        exportRoundScoresToDatabase("2019-20");
    }

    private void exportRoundScoresToDatabase(String seasonCode) throws URISyntaxException {

        System.out.println("Starting export for season " + seasonCode);
        List<String[]> roundScoresCsv = CsvUtils.readCsvWithoutHeaders(new File(this.getClass().getResource("/generated/" + seasonCode + ".csv").toURI()));
        Season season = seasonRepository.findByCode(seasonCode);

        roundScoresCsv.forEach(roundScoreCsv -> {

            RoundScore roundScore = new RoundScore();
            roundScore.setSeason(season);
            roundScore.setAssists(Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.ASSISTS.getIndexBySeason(seasonCode)]));
            roundScore.setBonus(Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.BONUS.getIndexBySeason(seasonCode)]));
            roundScore.setCleanSheets(Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.CLEAN_SHEETS.getIndexBySeason(seasonCode)]));
            roundScore.setCreativity(Double.parseDouble(roundScoreCsv[CsvUtils.CSV_INDEX.CREATIVITY.getIndexBySeason(seasonCode)]));
            roundScore.setGoals(Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.GOALS.getIndexBySeason(seasonCode)]));
            roundScore.setIct(Double.parseDouble(roundScoreCsv[CsvUtils.CSV_INDEX.ICT.getIndexBySeason(seasonCode)]));
            roundScore.setInfluence(Double.parseDouble(roundScoreCsv[CsvUtils.CSV_INDEX.INFLUENCE.getIndexBySeason(seasonCode)]));
            roundScore.setKickOff(ZonedDateTime.parse(roundScoreCsv[CsvUtils.CSV_INDEX.KICK_OFF.getIndexBySeason(seasonCode)]));
            roundScore.setMinutes(Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.MINUTES.getIndexBySeason(seasonCode)]));
            roundScore.setPoints(Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.POINTS.getIndexBySeason(seasonCode)]));
            roundScore.setRound(seasonCode.equals("2019-20") ? postCovidRound(Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.ROUND.getIndexBySeason(seasonCode)])) : Integer.parseInt(roundScoreCsv[CsvUtils.CSV_INDEX.ROUND.getIndexBySeason(seasonCode)]));
            roundScore.setThreat(Double.parseDouble(roundScoreCsv[CsvUtils.CSV_INDEX.THREAT.getIndexBySeason(seasonCode)]));
            String[] name = roundScoreCsv[CsvUtils.CSV_INDEX.NAME.getIndexBySeason(seasonCode)].split("_");
            roundScore.setFirstName(name[0]);
            roundScore.setLastName(name[1]);
            roundScore.setPlayer(playerRepository.findByFirstNameAndLastName(roundScore.getFirstName(), roundScore.getLastName()));
            roundScore.setSeasonRound(Integer.parseInt(seasonCode.replace("-", "") + String.format("%02d", roundScore.getRound())));
            roundScoreRepository.save(roundScore);
        });
        System.out.println("Finished export for season " + seasonCode);
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
                newPlayer.setCurrentPosition(Position.getPositionByCode(Integer.toString(playerJson.getElement_type())).name());
                playerRepository.save(newPlayer);

            } else {
                player.setCurrentTeam(currentTeam);
                playerRepository.save(player);
            }
        });
    }
}
