package com.fplstatistics.app;

import com.fplstatistics.app.knapsack.Knapsack;
import com.fplstatistics.app.player.PlayerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
    void positiveBudget_canAffordAll_SelectAll() {

        // given
        int budget = 100;

        assertThat(Knapsack.getDreamTeam(budget, getPlayersStub(), 3).getPlayers().size()).isEqualTo(3);
    }

    private List<PlayerDto> getPlayersStub() {
        return Arrays.asList(
                playerDto("Sterling", 10.0, 60.0),
                playerDto("De Bruyne", 20.0, 100.0),
                playerDto("Salah", 30.0, 120.0));
    }

    private PlayerDto playerDto(String webName, double price, double value) {
        PlayerDto playerDto = mock(PlayerDto.class);
        when(playerDto.getWebName()).thenReturn(webName);
        when(playerDto.getCost()).thenReturn(price);
        when(playerDto.getValue()).thenReturn(value);
        return playerDto;
    }
}
