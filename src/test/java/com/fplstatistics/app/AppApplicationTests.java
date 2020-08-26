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
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class AppApplicationTests {

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
        exportRoundScoresToDatabase("2018-19");
        exportRoundScoresToDatabase("2019-20");
    }

    @Test
    void createMergedFile() throws IOException {

        createMergedFile("2016-17", "assists,attempted_passes,big_chances_created,big_chances_missed,bonus,bps,clean_sheets,clearances_blocks_interceptions,completed_passes,creativity,dribbles,ea_index,element,errors_leading_to_goal,errors_leading_to_goal_attempt,fixture,fouls,goals_conceded,goals_scored,ict_index,id,influence,key_passes,kickoff_time,kickoff_time_formatted,loaned_in,loaned_out,minutes,offside,open_play_crosses,opponent_team,own_goals,penalties_conceded,penalties_missed,penalties_saved,recoveries,red_cards,round,saves,selected,tackled,tackles,target_missed,team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,winning_goals,yellow_cards");
        createMergedFile("2017-18", "assists,attempted_passes,big_chances_created,big_chances_missed,bonus,bps,clean_sheets,clearances_blocks_interceptions,completed_passes,creativity,dribbles,ea_index,element,errors_leading_to_goal,errors_leading_to_goal_attempt,fixture,fouls,goals_conceded,goals_scored,ict_index,id,influence,key_passes,kickoff_time,kickoff_time_formatted,loaned_in,loaned_out,minutes,offside,open_play_crosses,opponent_team,own_goals,penalties_conceded,penalties_missed,penalties_saved,recoveries,red_cards,round,saves,selected,tackled,tackles,target_missed,team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,winning_goals,yellow_cards");
        createMergedFile("2018-19", "assists,attempted_passes,big_chances_created,big_chances_missed,bonus,bps,clean_sheets,clearances_blocks_interceptions,completed_passes,creativity,dribbles,ea_index,element,errors_leading_to_goal,errors_leading_to_goal_attempt,fixture,fouls,goals_conceded,goals_scored,ict_index,id,influence,key_passes,kickoff_time,kickoff_time_formatted,loaned_in,loaned_out,minutes,offside,open_play_crosses,opponent_team,own_goals,penalties_conceded,penalties_missed,penalties_saved,recoveries,red_cards,round,saves,selected,tackled,tackles,target_missed,team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,winning_goals,yellow_cards");
        createMergedFile("2019-20", "assists,bonus,bps,clean_sheets,creativity,element,fixture,goals_conceded,goals_scored,ict_index,influence,kickoff_time,minutes,opponent_team,own_goals,penalties_missed,penalties_saved,red_cards,round,saves,selected,team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,yellow_cards");
    }

    private void createMergedFile(String season, String header) throws IOException {

        String rootDir = "/";
        String playersSourceDir = String.format("/data/%s/players", season);
        String mergedFile = String.format("generated/%s.csv", season);

        String mergedFileAbs = this.getClass().getResource(rootDir).getPath() + mergedFile;
        mergedFileAbs = mergedFileAbs.replace("target/test-classes", "src/main/resources");
        Path mergedFilePath = Paths.get(mergedFileAbs);

        Files.createDirectories(mergedFilePath.getParent());
        Files.deleteIfExists(mergedFilePath);
        Files.createFile(mergedFilePath);

        Files.write(mergedFilePath, ("name," + header + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        try (Stream<Path> paths = Files.walk(Paths.get(this.getClass().getResource(playersSourceDir).getPath()))) {
            List<Path> files = paths.filter(Files::isRegularFile).collect(Collectors.toList());
            files.forEach(file -> {
                List<String[]> playerRoundScores = readFromCSV(file.toFile());
                String playerDir = file.getParent().toString().substring(file.getParent().toString().lastIndexOf("/") + 1);
                playerRoundScores.forEach(playerRoundScore -> {
                    try {
                        StringBuilder line = new StringBuilder(playerDir).append(",");
                        for (String s : playerRoundScore) {
                            line.append(s).append(",");
                        }
                        line.append(System.lineSeparator());
                        Files.write(mergedFilePath, line.toString().getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });

                System.out.println("Finished processing " + file);
            });
        }
    }

    private void exportRoundScoresToDatabase(String seasonCode) throws URISyntaxException {

        List<String[]> roundScoresCsv = readFromCSV(new File(this.getClass().getResource("/merged-gw/" + seasonCode + ".csv").toURI()));
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
                newPlayer.setCurrentPosition(Position.getPositionByCode(Integer.toString(playerJson.getElement_type())).name());
                playerRepository.save(newPlayer);

            } else {
                player.setCurrentTeam(currentTeam);
                playerRepository.save(player);
            }
        });
    }

    private List<String[]> readFromCSV(File file) {
        List<String[]> lines;
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            lines = reader.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        lines.remove(0);
        return lines;
    }
}
