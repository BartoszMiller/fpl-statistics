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

        // when
        List<PlayerDto> goalkeepers = getPlayers(gameWeek, Integer.toString(Position.GKP.getCode()));
        List<PlayerDto> defenders = getPlayers(gameWeek, Integer.toString(Position.DEF.getCode()));
        List<PlayerDto> midfielders = getPlayers(gameWeek, Integer.toString(Position.MID.getCode()));
        List<PlayerDto> forwards = getPlayers(gameWeek, Integer.toString(Position.FWD.getCode()));

        int[][][] gkpTable = builder.buildTable(budget, goalkeepers, 1, PlayerDto::getPoints);
        int[][][] defTable = builder.buildTable(budget, defenders, 5, PlayerDto::getPoints);
        int[][][] midTable = builder.buildTable(budget, midfielders, 5, PlayerDto::getPoints);
        int[][][] fwdTable = builder.buildTable(budget, forwards, 3, PlayerDto::getPoints);

        double cheapestGkp = 4.0;
        double cheapestDef = 4.0;
        double cheapestMid = 4.5;
        double cheapestFwd = 4.5;

        int best = 0;
        for (double gkpBudget = 7; gkpBudget > 4; gkpBudget -= 0.5) {
            for (double defBudget = 40; defBudget > 11.5; defBudget -= 0.5) {
                for (double midBudget = 60; midBudget > 8.5; midBudget -= 0.5) {
                    for (double fwdBudget = 35; fwdBudget > 4; fwdBudget -= 0.5) {
                        if (gkpBudget + defBudget + midBudget + fwdBudget == 100) {
                            // 352
                            int maxGkp = gkpTable[gkpTable.length - 1][(int) (gkpBudget * 10) + 1][1];
                            int maxDef = defTable[defTable.length - 1][(int) (defBudget * 10) + 1][3];
                            int maxMid = midTable[midTable.length - 1][(int) (midBudget * 10) + 1][5];
                            int maxFwd = fwdTable[fwdTable.length - 1][(int) (fwdBudget * 10) + 1][2];
                            int total = maxGkp + maxDef + maxMid + maxFwd;
                            if (total > best) {
                                best = total;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("best:" + best);
        // int max = Collections.max(Arrays.asList(getResult(f352), getResult(f343), getResult(f433), getResult(f442), getResult(f451), getResult(f541), getResult(f532), getResult(f523)));
        DreamTeam dreamTeam0 = new DreamTeam(builder.getSelectedPlayers(gkpTable, goalkeepers, PlayerDto::getPoints));
        DreamTeam dreamTeam1 = new DreamTeam(builder.getSelectedPlayers(defTable, defenders, PlayerDto::getPoints));
        DreamTeam dreamTeam2 = new DreamTeam(builder.getSelectedPlayers(midTable, midfielders, PlayerDto::getPoints));
        DreamTeam dreamTeam3 = new DreamTeam(builder.getSelectedPlayers(fwdTable, forwards, PlayerDto::getPoints));

//        List<PlayerDto> selectedGoalkeepers = builder.getSelectedPlayers(tableGoalkeepers, goalkeepers, PlayerDto::getPoints);
//        DreamTeam dreamTeam = new DreamTeam(selectedGoalkeepers);

        // then
        System.out.println(dreamTeam0);
        System.out.println(dreamTeam1);
        System.out.println(dreamTeam2);
        System.out.println(dreamTeam3);
        // assertThat(max).isEqualTo(131);
        // assertThat(dreamTeam.getTeamPrice()).isEqualTo(83.0);
    }

    @Test
    void maxPoints_gw38() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek, null);
        int[][][] table = builder.buildTable(budget, players, 11, PlayerDto::getPoints);
        DreamTeam dreamTeam = new DreamTeam(builder.getSelectedPlayers(table, players, PlayerDto::getPoints));

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
        List<PlayerDto> selectedPlayers = builder.getSelectedPlayers(table, players, PlayerDto::getPoints);
        DreamTeam dreamTeam = new DreamTeam(selectedPlayers);

        // then
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(151);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(71.5);
    }

    private List<PlayerDto> getPlayers(int round, String positionCode) {
        return playerService.getPlayers("2019-20", "2019-20", round, round, new ArrayList<>(), positionCode, "90-100");
    }
}
