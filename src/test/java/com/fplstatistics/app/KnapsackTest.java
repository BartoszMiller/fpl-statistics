package com.fplstatistics.app;

import com.fplstatistics.app.knapsack.Knapsack;
import com.fplstatistics.app.player.PlayerDto;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KnapsackTest {

    @Test
    void zeroBudget_throwsException() {

        // given
        int budget = 0;

        Assertions.assertThrows(RuntimeException.class, () -> Knapsack.getDreamTeam(budget, getPlayersStub(), Integer.MAX_VALUE));
    }

    @Test
    void positiveBudget_cannotAffordAnyone() {

        // given
        int budget = 5;

        Assertions.assertThrows(RuntimeException.class, () -> Knapsack.getDreamTeam(budget, getPlayersStub(), Integer.MAX_VALUE));
    }

    @Test
    void positiveBudget_canAffordAll_WrongTeamNumber() {

        // given
        int budget = 100;

        Assertions.assertThrows(RuntimeException.class, () -> Knapsack.getDreamTeam(budget, getPlayersStub(), Integer.MAX_VALUE));
    }

    @Test
    void positiveBudget_canAffordCheapest() {

        // given
        int budget = 10;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 1).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("Sterling");
    }

    @Test
    void positiveBudget_canAffordTwo_SelectMoreValuable() {

        // given
        int budget = 20;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 1).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("De Bruyne");
    }

    @Test
    void positiveBudget_canAffordThree_SelectMoreValuable() {

        // given
        int budget = 30;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 1).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
    }

    @Test
    void positiveBudget_respectTeamSizeEvenWhenMoreValuablePlayerAvailable() {

        // given
        int budget = 30;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 2).getPlayers();

        assertThat(players.size()).isEqualTo(2);
        assertThat(players.get(0).getWebName()).isEqualTo("De Bruyne");
        assertThat(players.get(1).getWebName()).isEqualTo("Sterling");
    }

    @Test
    void positiveBudget_canAffordAll_SelectTwoForTeam() {

        // given
        int budget = 55;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 2).getPlayers();

        assertThat(players.size()).isEqualTo(2);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
        assertThat(players.get(1).getWebName()).isEqualTo("De Bruyne");
    }

    @Test
    void canAffordAll_RespectTeamSizeOfOne() {

        // given
        int budget = 100;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 1).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
    }

    @Test
    void canAffordAll_RespectTeamSizeOfTwo() {

        // given
        int budget = 100;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 2).getPlayers();

        assertThat(players.size()).isEqualTo(2);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
        assertThat(players.get(1).getWebName()).isEqualTo("De Bruyne");
    }

    @Test
    void canAffordAll_RespectTeamSizeOfThree() {

        // given
        int budget = 100;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStub(), 3).getPlayers();

        assertThat(players.size()).isEqualTo(3);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
        assertThat(players.get(1).getWebName()).isEqualTo("De Bruyne");
        assertThat(players.get(2).getWebName()).isEqualTo("Sterling");
    }

    @Test
    void positiveBudget_canAffordAll_SelectAll() {

        // given
        int budget = 100;

        assertThat(Knapsack.getDreamTeam(budget, getPlayersStub(), 3).getPlayers().size()).isEqualTo(3);
    }

    @Test
    void teamTest() {

        // given
        int budget = 100;

        List<PlayerDto> players = Knapsack.getDreamTeam(budget, getPlayersStubRespectFormation(), 11).getPlayers();
        List<String> names = players.stream().map(PlayerDto::getWebName).collect(Collectors.toList());
        assertThat(players.size()).isEqualTo(11);

        assertThat(names).contains("Dubravka", "Ings", "Jimenez", "Martial", "Sterling", "De Bruyne", "Salah", "Grosicki", "Robertson", "Van Dijk", "Alonso");
    }

    private List<PlayerDto> getPlayersStub() {
        return Arrays.asList(
                playerDto("Sterling", 10.0, 60.0, "MID"),
                playerDto("De Bruyne", 20.0, 100.0, "MID"),
                playerDto("Salah", 30.0, 120.0, "MID"),
                playerDto("Grosicki", 10.0, 1.0, "MID"),
                playerDto("Klich", 30.0, 1.0, "MID"));
    }

    private List<PlayerDto> getPlayersStubRespectFormation() {
        return Arrays.asList(
                playerDto("Dubravka", 1, 1000.0, "GKP"),
                playerDto("Pope", 1, 10.0, "GKP"),

                playerDto("Robertson", 1, 2000.0, "DEF"),
                playerDto("Van Dijk", 1, 2000.0, "DEF"),
                playerDto("Alonso", 1, 2000.0, "DEF"),
                playerDto("Doherty", 1, 2000.0, "DEF"),
                playerDto("Ake", 1, 2000.0, "DEF"),
                playerDto("Zinchenko", 1, 2000.0, "DEF"),

                playerDto("Sterling", 1, 1000.0, "MID"),
                playerDto("De Bruyne", 1, 1000.0, "MID"),
                playerDto("Salah", 1, 1000.0, "MID"),
                playerDto("Grosicki", 1, 1000.0, "MID"),
                playerDto("Klich", 1, 10.0, "MID"),
                playerDto("Mount", 1, 10.0, "MID"),

                playerDto("Aguero", 1, 1000.0, "FWD"),
                playerDto("Vardy", 1, 1000.0, "FWD"),
                playerDto("Martial", 1, 10.0, "FWD"),
                playerDto("Jimenez", 1, 10.0, "FWD"),
                playerDto("Ings", 1, 10.0, "FWD"));
    }

    private PlayerDto playerDto(String webName, double price, double value, String position) {
        PlayerDto playerDto = mock(PlayerDto.class);
        when(playerDto.getWebName()).thenReturn(webName);
        when(playerDto.getCost()).thenReturn(price);
        when(playerDto.getValue()).thenReturn(value);
        when(playerDto.getPosition()).thenReturn(position);
        return playerDto;
    }
}
