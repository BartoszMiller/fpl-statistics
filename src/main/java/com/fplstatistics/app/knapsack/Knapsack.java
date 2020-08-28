package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.position.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Knapsack {

    // A utility function that returns
    // maximum of two integers
    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static DreamTeam getDreamTeam(double budget, List<PlayerDto> players, int playersCount) {

        int costMultiplier = 10;
        int valueMultiplier = 100;

        int totalPlayers = players.size();
        double[] costs = new double[players.size()];
        double[] values = new double[players.size()];
        int[] positions = new int[players.size()];

        IntStream.range(0, players.size()).forEach(i -> {
            costs[i] = players.get(i).getCost();
            values[i] = players.get(i).getValue();
            positions[i] = Position.valueOf(players.get(i).getPosition()).getCode();
        });

        int[] wt = convertToIntArray(costs, costMultiplier);
        int[] val = convertToIntArray(values, valueMultiplier);

        List<Integer> indexes = printKnapsack((int) (budget * costMultiplier), wt, val, positions, totalPlayers, playersCount);
        return new DreamTeam(indexes.stream().map(players::get).collect(Collectors.toList()));
    }

    private static List<Integer> printKnapsack(int budget, int[] costs, int[] values, int[] positions, int numItems, int playersCount) {
        int i;
        int w;
        int k;
        int j;
        int[][][] costMatrix = buildKnapsackTable(budget, costs, values, numItems, playersCount);

        int bestValue = -1;
        int currentIndex = -1;
        int currentCost = -1;
        int currentCount = -1;
        int valueConstraint = Integer.MAX_VALUE;
        boolean validFormation = false;
        boolean ignoreFormation = playersCount != 11;
        List<Integer> indexes = null;

        while (!validFormation && valueConstraint > 0) {
            for (j = numItems; j > 0; j--) {
                for (w = 0; w < budget + 1; w++) {
                    for (k = 0; k < playersCount + 1; k++) {
                        int value = costMatrix[j][w][k];
                        if (((bestValue == -1) || (value > bestValue)) && value < valueConstraint) {
                            currentIndex = j;
                            currentCost = w;
                            currentCount = k;
                            bestValue = value;
                        }
                    }
                }
            }

            int res = costMatrix[currentIndex][currentCost][currentCount];

            indexes = new ArrayList<>();
            w = budget;
            k = playersCount;
            for (i = currentIndex; i > 0 && res > 0; i--) {
                if (k >= 0 && res > costMatrix[i - 1][w][k] && costMatrix[i - 1][w][k] >= 0) {
                    indexes.add(i - 1);
                    res = res - values[i - 1];
                    w = w - costs[i - 1];
                    k -= 1;
                }
            }

            if (!ignoreFormation) {
                if (indexes.size() == playersCount) {
                    List<Integer> positionCodes = indexes.stream().map(index -> positions[index]).collect(Collectors.toList());
                    validFormation =
                            Collections.frequency(positionCodes, 1) == 1
                                    && Collections.frequency(positionCodes, 2) >= 3 && Collections.frequency(positionCodes, 2) <= 5
                                    && Collections.frequency(positionCodes, 3) >= 2 && Collections.frequency(positionCodes, 3) <= 5
                                    && Collections.frequency(positionCodes, 4) >= 1 && Collections.frequency(positionCodes, 4) <= 3;
                }
                valueConstraint = bestValue - 1;
                bestValue = -1;
            } else {
                return indexes;
            }
        }
        if (indexes.size() != playersCount) {
            throw new RuntimeException(String.format("Dream Team is of different size. [Expected: %d player] [Actual: %d players]", playersCount, indexes.size()));
        }
        return indexes;
    }

    private static int[][][] buildKnapsackTable(int budget, int[] costs, int[] values, int numItems, int playersCount) {
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
                        costMatrix[i][w][k] = Math.max(
                                values[i - 1] + costMatrix[i - 1][w - costs[i - 1]][k - 1],
                                costMatrix[i - 1][w][k]);
                    } else {
                        costMatrix[i][w][k] = costMatrix[i - 1][w][k];
                    }
                }
            }
        }
        return costMatrix;
    }


    private static int[] convertToIntArray(double[] value, int multiplier) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * multiplier)).toArray();
    }
}
