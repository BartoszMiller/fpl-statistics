package com.fplstatistics.app;

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

    public static List<PlayerDto> getDreamTeam(double budget, List<PlayerDto> players) {

        int totalPlayers = players.size();
        double[] wtDouble = new double[players.size()];
        double[] valDouble = new double[players.size()];

        IntStream.range(0, players.size()).forEach(i -> {
            wtDouble[i] = players.get(i).getCost();
            valDouble[i] = players.get(i).getValue();
        });

        int[] wt = convertToIntArray(wtDouble);
        int[] val = convertToIntArray(valDouble);

        List<Integer> valuesMultiplied = printKnapsack((int) budget * 1000, wt, val, totalPlayers);
        List<Double> values = valuesMultiplied.stream().mapToDouble(i -> i / 1000).boxed().collect(Collectors.toList());

        return values
                .stream()
                .map(v -> players.stream().filter(p -> {
                    System.out.println(p.getValue());
                    return p.getValue() == v;
                }).findFirst().get())
                .collect(Collectors.toList());
    }

    private static List<Integer> printKnapsack(int W, int[] wt, int[] val, int n) {
        int i, w;
        int K[][] = new int[n + 1][W + 1];
        List<Integer> values = new ArrayList<>();

        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= W; w++) {
                if (i == 0 || w == 0)
                    K[i][w] = 0;
                else if (wt[i - 1] <= w)
                    K[i][w] = Math.max(val[i - 1] +
                            K[i - 1][w - wt[i - 1]], K[i - 1][w]);
                else
                    K[i][w] = K[i - 1][w];
            }
        }

        // stores the result of Knapsack
        int res = K[n][W];

        w = W;
        for (i = n; i > 0 && res > 0; i--) {

            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res == K[i - 1][w])
                continue;
            else {

                // This item is included.
                values.add(val[i - 1]);
                // Since this weight is included its
                // value is deducted
                res = res - val[i - 1];
                w = w - wt[i - 1];
            }
        }
        return values;
    }

    private static int[] convertToIntArray(double[] value) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * 1000)).toArray();
    }
}
