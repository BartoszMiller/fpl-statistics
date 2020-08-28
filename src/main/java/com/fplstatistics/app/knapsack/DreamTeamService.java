package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.position.Position;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DreamTeamService {

    public DreamTeam getBestPlayers(double budget, List<PlayerDto> players, int playersCount, ToDoubleFunction<PlayerDto> valueFunction) {
        return knapsack(budget, players, playersCount, false, valueFunction);
    }

    public DreamTeam getDreamEleven(double budget, List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {
        return knapsack(budget, players, 11, true, valueFunction);
    }

    private DreamTeam knapsack(double budget, List<PlayerDto> players, int playersCount, boolean validateFormation, ToDoubleFunction<PlayerDto> valueFunction) {

        int costMultiplier = 10;
        int valueMultiplier = 100;

        int totalPlayers = players.size();
        double[] costs = new double[players.size()];
        double[] values = new double[players.size()];
        int[] positions = new int[players.size()];

        IntStream.range(0, players.size()).forEach(i -> {
            costs[i] = players.get(i).getCost();
            values[i] = valueFunction.applyAsDouble(players.get(i));
            positions[i] = Position.valueOf(players.get(i).getPosition()).getCode();
        });

        int[] wt = convertToIntArray(costs, costMultiplier);
        int[] val = convertToIntArray(values, valueMultiplier);

        List<Integer> indexes = printKnapsack((int) (budget * costMultiplier), wt, val, positions, totalPlayers, playersCount, validateFormation);
        return new DreamTeam(indexes.stream().map(players::get).collect(Collectors.toList()));
    }

    private List<Integer> printKnapsack(int budget, int[] costs, int[] values, int[] positions, int numItems, int playersCount, boolean validateFormation) {
        int w;
        int k;
        int j;

        int[][][] costMatrix = buildKnapsackTable(budget, costs, values, positions, numItems, playersCount, validateFormation);

        int bestValue = -1;
        int currentIndex = -1;
        int currentCost = -1;
        int currentCount = -1;

        for (j = numItems; j > 0; j--) {
            for (w = 0; w < budget + 1; w++) {
                for (k = 0; k < playersCount + 1; k++) {
                    int value = costMatrix[j][w][k];
                    if (((bestValue == -1) || (value > bestValue))) {
                        currentIndex = j;
                        currentCost = w;
                        currentCount = k;
                        bestValue = value;
                    }
                }
            }
        }

        int res = costMatrix[currentIndex][currentCost][currentCount];
        List<Integer> indexes = getIndexes(costs, values, budget, playersCount, costMatrix, currentIndex, res);
        if (indexes.size() != playersCount) {
            throw new RuntimeException(String.format("Dream Team is of different size. [Expected: %d player] [Actual: %d players]", playersCount, indexes.size()));
        }
        return indexes;
    }

    private List<Integer> getIndexes(int[] costs, int[] values, int w, int k, int[][][] costMatrix, int currentIndex, int res) {
        List<Integer> indexes = new ArrayList<>();
        int i;
        for (i = currentIndex; i > 0 && res > 0; i--) {
            if (k >= 0 && res > costMatrix[i - 1][w][k] && costMatrix[i - 1][w][k] >= 0) {
                indexes.add(i - 1);
                res = res - values[i - 1];
                w = w - costs[i - 1];
                k -= 1;
            }
        }
        return indexes;
    }

    private int[][][] buildKnapsackTable(int budget, int[] costs, int[] values, int[] positionsCodes, int numItems, int playersCount, boolean validateFormation) {
        int i;
        int w;
        int k;
        int[][][] costMatrix = new int[numItems + 1][budget + 1][playersCount + 1];

        for (i = 0; i <= numItems; i++) {
            for (w = 0; w <= budget; w++) {
                for (k = 0; k <= playersCount; k++) {
                    if (i == 0 || w == 0 || k == 0) {
                        costMatrix[i][w][k] = 0;
                    } else if ((costs[i - 1] <= w)) {

                        int nextValue = Math.max(
                                values[i - 1] + costMatrix[i - 1][w - costs[i - 1]][k - 1],
                                costMatrix[i - 1][w][k]);
                        costMatrix[i][w][k] = nextValue;

                        if (validateFormation) {
                            List<Integer> indexes = getIndexes(costs, values, w, k, costMatrix, i, costMatrix[i][w][k]);
                            List<Position> positions = indexes.stream().map(index -> Position.getPositionByCode(Integer.toString(positionsCodes[index]))).collect(Collectors.toList());

                            if (isBeyondPositionLimits(positions)) {
                                costMatrix[i][w][k] = costMatrix[i - 1][w][k];
                            }
                        }

                    } else {
                        costMatrix[i][w][k] = costMatrix[i - 1][w][k];
                    }
                }
            }
        }
        return costMatrix;
    }

    private boolean isBeyondPositionLimits(List<Position> positions) {
        return (Collections.frequency(positions, Position.DEF) == 5 && Collections.frequency(positions, Position.MID) == 5) || // avoid 5-5-0 formation
                (Collections.frequency(positions, Position.DEF) == 2 && Collections.frequency(positions, Position.MID) == 5) || // avoid 2-5-3 formation
                (Collections.frequency(positions, Position.GKP) > 1 || Collections.frequency(positions, Position.DEF) > 5 ||
                        Collections.frequency(positions, Position.MID) > 5 || Collections.frequency(positions, Position.FWD) > 3);
    }

    private int[] convertToIntArray(double[] value, int multiplier) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * multiplier)).toArray();
    }
}
