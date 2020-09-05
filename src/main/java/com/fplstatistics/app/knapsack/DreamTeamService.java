package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.position.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Service
public class DreamTeamService {

    private final KnapsackTableBuilder knapsackTableBuilder;

    @Autowired
    public DreamTeamService(KnapsackTableBuilder knapsackTableBuilder) {
        this.knapsackTableBuilder = knapsackTableBuilder;
    }

    public DreamTeam getBestPlayers(double budget, List<PlayerDto> players, int playersCount, ToDoubleFunction<PlayerDto> valueFunction) {
        int[][][] table = knapsackTableBuilder.buildTable(budget, players, playersCount, valueFunction);
        List<PlayerDto> playersForBestCell = knapsackTableBuilder.getPlayersForBestCell(table, players, PlayerDto::getValue);
        return new DreamTeam(playersForBestCell);
    }

    public DreamTeam getDreamEleven(double budget, List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {

        List<PlayerDto> allGoalkeepers = players.stream().filter(p -> p.getPosition().equals(Position.GKP.name())).collect(Collectors.toList());
        List<PlayerDto> allDefenders = players.stream().filter(p -> p.getPosition().equals(Position.DEF.name())).collect(Collectors.toList());
        List<PlayerDto> allMidfielders = players.stream().filter(p -> p.getPosition().equals(Position.MID.name())).collect(Collectors.toList());
        List<PlayerDto> allForwards = players.stream().filter(p -> p.getPosition().equals(Position.FWD.name())).collect(Collectors.toList());

        int[][][] gkpTable = knapsackTableBuilder.buildTable(budget, allGoalkeepers, 1, valueFunction);
        int[][][] defTable = knapsackTableBuilder.buildTable(budget, allDefenders, 5, valueFunction);
        int[][][] midTable = knapsackTableBuilder.buildTable(budget, allMidfielders, 5, valueFunction);
        int[][][] fwdTable = knapsackTableBuilder.buildTable(budget, allForwards, 3, valueFunction);

        int best = 0;

        int gkpW = 0;
        int gkpK = 0;
        int defW = 0;
        int defK = 0;
        int midW = 0;
        int midK = 0;
        int fwdW = 0;
        int fwdK = 0;

        for (double gkpBudget = 20; gkpBudget > 0; gkpBudget -= 0.5) {
            for (double defBudget = 40; defBudget > 0; defBudget -= 0.5) {
                for (double midBudget = 60; midBudget > 0; midBudget -= 0.5) {
                    for (double fwdBudget = 40; fwdBudget > 0; fwdBudget -= 0.5) {
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

        List<PlayerDto> goalkeeper = knapsackTableBuilder.getPlayersForAnyCell(gkpTable, gkpTable.length - 1, gkpW, gkpK, allGoalkeepers, valueFunction);
        List<PlayerDto> defenders = knapsackTableBuilder.getPlayersForAnyCell(defTable, defTable.length - 1, defW, defK, allDefenders, valueFunction);
        List<PlayerDto> midfielders = knapsackTableBuilder.getPlayersForAnyCell(midTable, midTable.length - 1, midW, midK, allMidfielders, valueFunction);
        List<PlayerDto> forwards = knapsackTableBuilder.getPlayersForAnyCell(fwdTable, fwdTable.length - 1, fwdW, fwdK, allForwards, valueFunction);

        return new DreamTeam(goalkeeper, defenders, midfielders, forwards);
    }

    private boolean isNotRespectingFormationAndClubLimits(List<Position> positions, List<String> clubsToValidate) {

        Map<String, Long> clubsToCount = clubsToValidate.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return clubsToCount.values().stream().anyMatch(count -> count > 3) ||
                (Collections.frequency(positions, Position.DEF) == 5 && Collections.frequency(positions, Position.MID) == 5) || // reject 5-5-0 formation
                (Collections.frequency(positions, Position.MID) == 5 && Collections.frequency(positions, Position.FWD) == 3) || // reject 2-5-3 formation
                (Collections.frequency(positions, Position.DEF) == 4 && Collections.frequency(positions, Position.MID) == 5 && Collections.frequency(positions, Position.FWD) == 2) || // reject 4-5-2 formation
                (Collections.frequency(positions, Position.DEF) == 4 && Collections.frequency(positions, Position.MID) == 4 && Collections.frequency(positions, Position.FWD) == 3) || // reject 4-4-3 formation
                (Collections.frequency(positions, Position.DEF) == 5 && Collections.frequency(positions, Position.MID) == 4 && Collections.frequency(positions, Position.FWD) == 2) || // reject 5-4-2 formation
                (Collections.frequency(positions, Position.DEF) == 5 && Collections.frequency(positions, Position.MID) == 3 && Collections.frequency(positions, Position.FWD) == 3) || // reject 5-3-3 formation
                Collections.frequency(positions, Position.GKP) > 1 ||
                Collections.frequency(positions, Position.DEF) > 5 ||
                Collections.frequency(positions, Position.MID) > 5 ||
                Collections.frequency(positions, Position.FWD) > 3;
    }

    private int[] convertToIntArray(double[] value, int multiplier) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * multiplier)).toArray();
    }
}
