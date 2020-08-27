package com.fplstatistics.app;

import com.fplstatistics.app.player.PlayerDto;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KnapsackTest {

    public static void main(String[] arg) {

        List<PlayerDto> playerDtos = Arrays.asList(
                playerDto("Sterling", 10.0, 60.0),
                playerDto("De Bruyne", 20.0, 100.0),
                playerDto("Salah", 30.0, 120.0));

        double budget = 60.0;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, playerDtos);
        players.forEach(player -> System.out.println(player.getWebName()));
    }

    private static PlayerDto playerDto(String webName, double price, double value) {
        PlayerDto playerDto = mock(PlayerDto.class);
        when(playerDto.getWebName()).thenReturn(webName);
        when(playerDto.getCost()).thenReturn(price);
        when(playerDto.getValue()).thenReturn(value);
        return playerDto;
    }

    private static int[] convertToInt(double[] value) {
        return Arrays.stream(value).mapToInt(d -> (int) (d * 1000)).toArray();
    }
}
