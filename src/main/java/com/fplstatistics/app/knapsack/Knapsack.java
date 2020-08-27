package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;

import java.util.ArrayList;
import java.util.Arrays;
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

        IntStream.range(0, players.size()).forEach(i -> {
            costs[i] = players.get(i).getCost();
            values[i] = players.get(i).getValue();
        });

        int[] wt = convertToIntArray(costs, costMultiplier);
        int[] val = convertToIntArray(values, valueMultiplier);

        List<Integer> indexes = printKnapsack((int) (budget * costMultiplier), wt, val, totalPlayers, playersCount);
        return new DreamTeam(indexes.stream().map(players::get).collect(Collectors.toList()));
    }

    private static List<Integer> printKnapsack(int budget, int[] costs, int[] values, int num_items, int playersCount) {
        List<Integer> indexes = new ArrayList<>();
        int i, w, k;
        int[][][] cost_matrix = new int[num_items + 1][budget + 1][playersCount + 1];

        for (i = 0; i <= num_items; i++) {
            for (w = 0; w <= budget; w++) {
                for (k = 0; k <= playersCount; k++) {
                    if (i == 0 || w == 0 || k == 0) {
                        cost_matrix[i][w][k] = 0;
                    } else if ((costs[i - 1] <= w)) {
                        cost_matrix[i][w][k] = Math.max(
                                values[i - 1] + cost_matrix[i - 1][w - costs[i - 1]][k - 1],
                                cost_matrix[i - 1][w][k]);
                    } else {
                        cost_matrix[i][w][k] = cost_matrix[i - 1][w][k];
                    }
                }
            }
        }

        int bestValue = -1;
        int currentCost = -1;
        int currentCount = -1;

        for (w = 0; w < budget + 1; w++) {
            for (k = 0; k < playersCount + 1; k++) {
                int value = cost_matrix[num_items][w][k];
                if ((bestValue == -1) || (value > bestValue)) {
                    currentCost = w;
                    currentCount = k;
                    bestValue = value;
                }
            }
        }

        int res = cost_matrix[num_items][currentCost][currentCount];

        w = budget;
        k = playersCount;
        for (i = num_items; i > 0 && res > 0; i--) {

            if (res == cost_matrix[i - 1][w][k]) {

            } else {
                indexes.add(i - 1);
                res = res - values[i - 1];
                w = w - costs[i - 1];
                k -= 1;
            }
        }
        if (indexes.size() != playersCount) {
            throw new RuntimeException(String.format("Dream Team is of different size. [Expected: %d player] [Actual: %d players]", playersCount, indexes.size()));
        } else {
            return indexes;
        }
    }


    private static int[] convertToIntArray(double[] value, int multiplier) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * multiplier)).toArray();
    }
}
