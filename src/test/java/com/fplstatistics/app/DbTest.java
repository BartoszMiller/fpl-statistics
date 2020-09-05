package com.fplstatistics.app;

import com.fplstatistics.app.knapsack.DreamTeam;
import com.fplstatistics.app.knapsack.KnapsackTableBuilder;
import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.player.PlayerService;
import com.fplstatistics.app.position.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DbTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private KnapsackTableBuilder builder;

    @Test
    void maxPoints_gw38_mergeTables() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // input

        // when
        List<PlayerDto> allGoalkeepers = getPlayers(gameWeek, Integer.toString(Position.GKP.getCode()));
        List<PlayerDto> allDefenders = getPlayers(gameWeek, Integer.toString(Position.DEF.getCode()));
        List<PlayerDto> allMidfielders = getPlayers(gameWeek, Integer.toString(Position.MID.getCode()));
        List<PlayerDto> allForwards = getPlayers(gameWeek, Integer.toString(Position.FWD.getCode()));

        int[][][] gkpTable = builder.buildTable(budget, allGoalkeepers, 1, PlayerDto::getPoints);
        int[][][] defTable = builder.buildTable(budget, allDefenders, 5, PlayerDto::getPoints);
        int[][][] midTable = builder.buildTable(budget, allMidfielders, 5, PlayerDto::getPoints);
        int[][][] fwdTable = builder.buildTable(budget, allForwards, 3, PlayerDto::getPoints);

        int best = 0;

        int gkpW = 0;
        int gkpK = 0;
        int defW = 0;
        int defK = 0;
        int midW = 0;
        int midK = 0;
        int fwdW = 0;
        int fwdK = 0;

        for (double gkpBudget = 7; gkpBudget > 4; gkpBudget -= 0.5) {
            for (double defBudget = 40; defBudget > 11.5; defBudget -= 0.5) {
                for (double midBudget = 60; midBudget > 8.5; midBudget -= 0.5) {
                    for (double fwdBudget = 35; fwdBudget > 4; fwdBudget -= 0.5) {
                        if (gkpBudget + defBudget + midBudget + fwdBudget == budget) {

                            int maxGkp = gkpTable[gkpTable.length - 1][(int) (gkpBudget * 10) + 1][1];

                            int maxDef3 = defTable[defTable.length - 1][(int) (defBudget * 10) + 1][3];
                            int maxDef4 = defTable[defTable.length - 1][(int) (defBudget * 10) + 1][4];
                            int maxDef5 = defTable[defTable.length - 1][(int) (defBudget * 10) + 1][5];

                            int maxMid2 = midTable[midTable.length - 1][(int) (midBudget * 10) + 1][2];
                            int maxMid3 = midTable[midTable.length - 1][(int) (midBudget * 10) + 1][3];
                            int maxMid4 = midTable[midTable.length - 1][(int) (midBudget * 10) + 1][4];
                            int maxMid5 = midTable[midTable.length - 1][(int) (midBudget * 10) + 1][5];

                            int maxFwd1 = fwdTable[fwdTable.length - 1][(int) (fwdBudget * 10) + 1][1];
                            int maxFwd2 = fwdTable[fwdTable.length - 1][(int) (fwdBudget * 10) + 1][2];
                            int maxFwd3 = fwdTable[fwdTable.length - 1][(int) (fwdBudget * 10) + 1][3];

                            int f352 = maxGkp + maxDef3 + maxMid5 + maxFwd2;
                            int f343 = maxGkp + maxDef3 + maxMid4 + maxFwd3;
                            int f451 = maxGkp + maxDef4 + maxMid5 + maxFwd1;
                            int f442 = maxGkp + maxDef4 + maxMid4 + maxFwd2;
                            int f433 = maxGkp + maxDef4 + maxMid3 + maxFwd3;
                            int f541 = maxGkp + maxDef5 + maxMid4 + maxFwd1;
                            int f532 = maxGkp + maxDef5 + maxMid3 + maxFwd2;
                            int f523 = maxGkp + maxDef5 + maxMid2 + maxFwd3;

                            List<Integer> formations = Arrays.asList(f352, f343, f451, f442, f433, f541, f532, f523);
                            Integer maxFormation = Collections.max(formations);

                            if (maxFormation > best) {

                                best = maxFormation;

                                gkpK = 1;
                                gkpW = (int) (gkpBudget * 10) + 1;
                                defW = (int) (defBudget * 10) + 1;
                                midW = (int) (midBudget * 10) + 1;
                                fwdW = (int) (fwdBudget * 10) + 1;

                                int i = formations.indexOf(maxFormation);
                                if (i == 0) {
                                    defK = 3;
                                    midK = 5;
                                    fwdK = 2;
                                } else if (i == 1) {
                                    defK = 3;
                                    midK = 4;
                                    fwdK = 3;
                                } else if (i == 2) {
                                    defK = 4;
                                    midK = 5;
                                    fwdK = 1;
                                } else if (i == 3) {
                                    defK = 4;
                                    midK = 4;
                                    fwdK = 2;
                                } else if (i == 4) {
                                    defK = 4;
                                    midK = 3;
                                    fwdK = 3;
                                } else if (i == 5) {
                                    defK = 5;
                                    midK = 4;
                                    fwdK = 1;
                                } else if (i == 6) {
                                    defK = 5;
                                    midK = 3;
                                    fwdK = 2;
                                } else if (i == 7) {
                                    defK = 5;
                                    midK = 2;
                                    fwdK = 3;
                                }
                            }
                        }
                    }
                }
            }
        }

        List<PlayerDto> goalkeeper = builder.getPlayersForAnyCell(gkpTable, gkpTable.length - 1, gkpW, gkpK, allGoalkeepers, PlayerDto::getPoints);
        List<PlayerDto> defenders = builder.getPlayersForAnyCell(defTable, defTable.length - 1, defW, defK, allDefenders, PlayerDto::getPoints);
        List<PlayerDto> midfielders = builder.getPlayersForAnyCell(midTable, midTable.length - 1, midW, midK, allMidfielders, PlayerDto::getPoints);
        List<PlayerDto> forwards = builder.getPlayersForAnyCell(fwdTable, fwdTable.length - 1, fwdW, fwdK, allForwards, PlayerDto::getPoints);

        DreamTeam dreamTeam = new DreamTeam(goalkeeper, defenders, midfielders, forwards);

        // then
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(131);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(83.0);
    }

    @Test
    void maxPoints_gw38() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek, null);
        int[][][] table = builder.buildTable(budget, players, 11, PlayerDto::getPoints);
        DreamTeam dreamTeam = new DreamTeam(builder.getPlayersForBestCell(table, players, PlayerDto::getPoints));

        // then
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(131);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(83.0);
    }

    @Test
    void maxPoints_gw35() {

        // given
        int gameWeek = 35;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek, null);
        int[][][] table = builder.buildTable(budget, players, 11, PlayerDto::getPoints);
        List<PlayerDto> selectedPlayers = builder.getPlayersForBestCell(table, players, PlayerDto::getPoints);
        DreamTeam dreamTeam = new DreamTeam(selectedPlayers);

        // then
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(151);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(71.5);
    }

    private List<PlayerDto> getPlayers(int round, String positionCode) {
        return playerService.getPlayers("2019-20", "2019-20", round, round, new ArrayList<>(), positionCode, "90-100");
    }
}
