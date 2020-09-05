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

    private static final int COST_MULTIPLIER = 10;
    private static final int VALUE_MULTIPLIER = 100;

    public int[][][] buildTable(double maxWeight, List<PlayerDto> players, int playersCount, ToDoubleFunction<PlayerDto> valueFunction) {

        int multipliedMaxWeight = (int) maxWeight * COST_MULTIPLIER;
        int[] weights = getWeightsFromPlayers(players);
        int[] values = getValuesFromPlayers(players, valueFunction);

        int numberOfItems = weights.length;
        int[][][] table = new int[numberOfItems + 1][multipliedMaxWeight + 1][playersCount + 1];

        for (int n = 0; n <= numberOfItems; n++) {
            for (int w = 0; w <= multipliedMaxWeight; w++) {
                for (int k = 0; k <= playersCount; k++) {
                    if (n == 0 || w == 0 || k == 0) {
                        table[n][w][k] = 0;
                    } else if (weights[n - 1] <= w) {
                        table[n][w][k] = Math.max(
                                table[n - 1][w][k],
                                table[n - 1][w - weights[n - 1]][k - 1] + values[n - 1]);
                    } else {
                        table[n][w][k] = table[n - 1][w][k];
                    }
                }
            }
        }
        return table;
    }

    public List<PlayerDto> getPlayersForBestCell(int[][][] table, List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {
        return traversePlayersInTable(table, table.length - 1, table[0].length - 1, table[0][0].length - 1, players, valueFunction);
    }

    public List<PlayerDto> getPlayersForAnyCell(int[][][] table, int n, int w, int k, List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {
        return traversePlayersInTable(table, n, w, k, players, valueFunction);
    }

    private List<PlayerDto> traversePlayersInTable(int[][][] table, int n, int w, int k, List<PlayerDto> players, ToDoubleFunction<PlayerDto> valueFunction) {
        int[] weights = getWeightsFromPlayers(players);
        int[] values = getValuesFromPlayers(players, valueFunction);

        while (w > 0 && table[n][w - 1][k] == table[n][w][k]) {
            w--;
        }

        int result = table[n][w][k];
        List<Integer> indexes = new ArrayList<>();

        for (; n > 0 && result > 0; n--) {

            // either the result comes from the top (K[i-1][w][k]) if so, then item not included
            // otherwise, included
            if (result != table[n - 1][w][k]) {
                result = result - values[n - 1];
                w = w - weights[n - 1];
                k -= 1;
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
