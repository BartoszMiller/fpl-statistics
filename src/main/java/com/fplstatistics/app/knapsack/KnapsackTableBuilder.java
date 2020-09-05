package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class KnapsackTableBuilder {

    private static final int COST_MULTIPLIER = 1;
    private static final int VALUE_MULTIPLIER = 1;

    int[][] buildTable(int maxWeight, List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {

        maxWeight *= COST_MULTIPLIER;
        int[] weights = getWeightsFromPlayers(players);
        int[] values = getValuesFromPlayers(players, valueFunction);

        int numberOfItems = weights.length;
        int[][] table = new int[numberOfItems + 1][maxWeight + 1];

        for (int n = 0; n <= numberOfItems; n++) {
            for (int w = 0; w <= maxWeight; w++) {
                if (n == 0 || w == 0) {
                    table[n][w] = 0;
                } else if (weights[n - 1] <= w) {
                    table[n][w] = Math.max(
                            table[n - 1][w],
                            table[n - 1][w - weights[n - 1]] + values[n - 1]);
                } else {
                    table[n][w] = table[n - 1][w];
                }
            }
        }
        return table;
    }

    List<PlayerDto> getSelectedPlayers(int[][] table, List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {

        int[] weights = getWeightsFromPlayers(players);
        int[] values = getValuesFromPlayers(players, valueFunction);

        int result = table[table.length - 1][table[0].length - 1];
        int w = table[0].length - 1;
        List<Integer> indexes = new ArrayList<>();

        for (int n = table.length - 1; n > 0 && result > 0; n--) {

            // either the result comes from the top (K[i-1][w]) if so, then item not included
            // otherwise, included
            if (result != table[n - 1][w]) {
                result = result - values[n - 1];
                w = w - weights[n - 1];
                indexes.add(n - 1);
            }
        }
        return indexes.stream().map(players::get).collect(Collectors.toList());
    }

    private int[] getWeightsFromPlayers(List<PlayerDto> players) {
        double[] weights = new double[players.size()];
        IntStream.range(0, players.size()).forEach(i -> weights[i] = players.get(i).getCost());
        return convertToIntArray(weights, COST_MULTIPLIER);
    }

    private int[] getValuesFromPlayers(List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {
        double[] values = new double[players.size()];
        IntStream.range(0, players.size()).forEach(i -> values[i] = valueFunction.applyAsDouble(players.get(i)));
        return convertToIntArray(values, VALUE_MULTIPLIER);
    }

    private int[] convertToIntArray(double[] value, int multiplier) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * multiplier)).toArray();
    }
}