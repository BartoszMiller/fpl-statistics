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
        int i, w, c;
        //int[][][] cost_matrix = new int[num_items + 1][budget + 1][playersCount + 1];
        int[][] cost_matrix = new int[num_items + 1][budget + 1];

        for (i = 0; i <= num_items; i++) {
            for (w = 0; w <= budget; w++) {
                if (i == 0 || w == 0) {
                    cost_matrix[i][w] = 0;
                } else if ((costs[i - 1] <= w)) {
                    cost_matrix[i][w] = Math.max(
                            values[i - 1] + cost_matrix[i - 1][w - costs[i - 1]],
                            cost_matrix[i - 1][w]);
                } else {
                    cost_matrix[i][w] = cost_matrix[i - 1][w];
                }
            }
        }
//                for (c = 0; c <= playersCount; c++) {
//                    if (i == 0 || w == 0 || c == 0) {
//                        cost_matrix[i][w][c] = 0;
//                    } else if ((cost[i - 1] <= w) || (c >= 1)) {
//                        cost_matrix[i][w][c] = Math.max(
//                                items[i - 1] + cost_matrix[i - 1][w - cost[i - 1]][c - 1],
//                                cost_matrix[i - 1][w][c]);
//                    } else {
//                        cost_matrix[i][w][c] = cost_matrix[i - 1][w][c];
//                    }
//                }


        int bestValue = -1;
        int currentCost = -1;
        //int currentCount = -1;
        int[] marked = new int[num_items];

        for (w = 0; w < budget + 1; w++) {
            //for (c = 0; c < playersCount + 1; c++) {
            int value = cost_matrix[num_items][w];
            if ((bestValue == -1) || (value > bestValue)) {
                currentCost = w;
                bestValue = value;
            }
            //}
        }

        int res = cost_matrix[num_items][currentCost];

        w = budget;
        for (i = num_items; i > 0 && res > 0; i--) {

            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res == cost_matrix[i - 1][w])
                continue;
            else {

                // This item is included.
                indexes.add(i - 1);
                // Since this weight is included its
                // value is deducted
                res = res - values[i - 1];
                w = w - costs[i - 1];
            }
        }
        if (indexes.size() != playersCount) {
            throw new RuntimeException(String.format("Dream Team is of different size. [Expected: %d player] [Actual: %d players]", playersCount, indexes.size()));
        } else {
            return indexes;
        }

//        while (itemIndex >= 0 && currentCost >= 0) {
//            if ((itemIndex == 0 && cost_matrix[itemIndex][currentCost] > 0) ||
//                    ((cost_matrix[itemIndex][currentCost] != cost_matrix[itemIndex - 1][currentCost]))) {
//                marked[itemIndex] = 1;
//                currentCost -= costs[itemIndex];
//            }
//            itemIndex -= 1;
//
//        }
//        return marked;
    }

    private static int[] convertToIntArray(double[] value, int multiplier) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * multiplier)).toArray();
    }
}
