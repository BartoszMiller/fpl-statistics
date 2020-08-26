package com.fplstatistics.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class GwMergedFileGenerator {

    @Test
    void createMergedFile() throws IOException {

        createMergedFile("2016-17", "assists,attempted_passes,big_chances_created,big_chances_missed,bonus,bps,clean_sheets,clearances_blocks_interceptions,completed_passes,creativity,dribbles,ea_index,element,errors_leading_to_goal,errors_leading_to_goal_attempt,fixture,fouls,goals_conceded,goals_scored,ict_index,id,influence,key_passes,kickoff_time,kickoff_time_formatted,loaned_in,loaned_out,minutes,offside,open_play_crosses,opponent_team,own_goals,penalties_conceded,penalties_missed,penalties_saved,recoveries,red_cards,round,saves,selected,tackled,tackles,target_missed,team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,winning_goals,yellow_cards");
        createMergedFile("2017-18", "assists,attempted_passes,big_chances_created,big_chances_missed,bonus,bps,clean_sheets,clearances_blocks_interceptions,completed_passes,creativity,dribbles,ea_index,element,errors_leading_to_goal,errors_leading_to_goal_attempt,fixture,fouls,goals_conceded,goals_scored,ict_index,id,influence,key_passes,kickoff_time,kickoff_time_formatted,loaned_in,loaned_out,minutes,offside,open_play_crosses,opponent_team,own_goals,penalties_conceded,penalties_missed,penalties_saved,recoveries,red_cards,round,saves,selected,tackled,tackles,target_missed,team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,winning_goals,yellow_cards");
        createMergedFile("2018-19", "assists,attempted_passes,big_chances_created,big_chances_missed,bonus,bps,clean_sheets,clearances_blocks_interceptions,completed_passes,creativity,dribbles,ea_index,element,errors_leading_to_goal,errors_leading_to_goal_attempt,fixture,fouls,goals_conceded,goals_scored,ict_index,id,influence,key_passes,kickoff_time,kickoff_time_formatted,loaned_in,loaned_out,minutes,offside,open_play_crosses,opponent_team,own_goals,penalties_conceded,penalties_missed,penalties_saved,recoveries,red_cards,round,saves,selected,tackled,tackles,target_missed,team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,winning_goals,yellow_cards");
        createMergedFile("2019-20", "assists,                                                        bonus,bps,clean_sheets,                                                 creativity,                  element,                                                      fixture,      goals_conceded,goals_scored,ict_index,   influence,           kickoff_time,                                            minutes,                          opponent_team,own_goals,                   penalties_missed,penalties_saved,           red_cards,round,saves,selected,                              team_a_score,team_h_score,threat,total_points,transfers_balance,transfers_in,transfers_out,value,was_home,              yellow_cards");
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
                List<String[]> playerRoundScores = CsvUtils.readCsvWithoutHeaders(file.toFile());
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
}
