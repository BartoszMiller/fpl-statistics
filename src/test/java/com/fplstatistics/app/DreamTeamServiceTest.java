package com.fplstatistics.app;

import com.fplstatistics.app.knapsack.DreamTeam;
import com.fplstatistics.app.knapsack.DreamTeamService;
import com.fplstatistics.app.knapsack.KnapsackTableBuilder;
import com.fplstatistics.app.player.PlayerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DreamTeamServiceTest {

    private final DreamTeamService dreamTeamService = new DreamTeamService(new KnapsackTableBuilder());

    @Test
    void zeroBudget_throwsException() {

        // given
        int budget = 0;

        Assertions.assertThrows(RuntimeException.class, () -> dreamTeamService.getBestPlayers(budget, getPlayersStub(), Integer.MAX_VALUE, PlayerDto::getValue));
    }

    @Test
    void positiveBudget_cannotAffordAnyone() {

        // given
        int budget = 5;

        Assertions.assertThrows(RuntimeException.class, () -> dreamTeamService.getBestPlayers(budget, getPlayersStub(), Integer.MAX_VALUE, PlayerDto::getValue));
    }

    @Test
    void positiveBudget_canAffordAll_WrongTeamNumber() {

        // given
        int budget = 100;

        Assertions.assertThrows(RuntimeException.class, () -> dreamTeamService.getBestPlayers(budget, getPlayersStub(), Integer.MAX_VALUE, PlayerDto::getValue));
    }

    @Test
    void positiveBudget_canAffordCheapest() {

        // given
        int budget = 10;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 1, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("Sterling");
    }

    @Test
    void positiveBudget_canAffordTwo_SelectMoreValuable() {

        // given
        int budget = 20;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 1, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("De Bruyne");
    }

    @Test
    void positiveBudget_canAffordThree_SelectMoreValuable() {

        // given
        int budget = 30;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 1, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
    }

    @Test
    void positiveBudget_respectTeamSizeEvenWhenMoreValuablePlayerAvailable() {

        // given
        int budget = 30;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 2, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(2);
        assertThat(players.get(0).getWebName()).isEqualTo("De Bruyne");
        assertThat(players.get(1).getWebName()).isEqualTo("Sterling");
    }

    @Test
    void positiveBudget_canAffordAll_SelectTwoForTeam() {

        // given
        int budget = 55;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 2, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(2);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
        assertThat(players.get(1).getWebName()).isEqualTo("De Bruyne");
    }

    @Test
    void canAffordAll_RespectTeamSizeOfOne() {

        // given
        int budget = 100;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 1, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(1);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
    }

    @Test
    void canAffordAll_RespectTeamSizeOfTwo() {

        // given
        int budget = 100;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 2, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(2);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
        assertThat(players.get(1).getWebName()).isEqualTo("De Bruyne");
    }

    @Test
    void canAffordAll_RespectTeamSizeOfThree() {

        // given
        int budget = 100;

        List<PlayerDto> players = dreamTeamService.getBestPlayers(budget, getPlayersStub(), 3, PlayerDto::getValue).getPlayers();

        assertThat(players.size()).isEqualTo(3);
        assertThat(players.get(0).getWebName()).isEqualTo("Salah");
        assertThat(players.get(1).getWebName()).isEqualTo("De Bruyne");
        assertThat(players.get(2).getWebName()).isEqualTo("Sterling");
    }

    @Test
    void positiveBudget_canAffordAll_SelectAll() {

        // given
        int budget = 100;

        assertThat(dreamTeamService.getBestPlayers(budget, getPlayersStub(), 3, PlayerDto::getValue).getPlayers().size()).isEqualTo(3);
    }

    @Test
    void dreamTeam_respectFormationAndClubs() {

        // given
        int budget = 15;

        List<PlayerDto> players = dreamTeamService.getDreamEleven(budget, getPlayersStubRespectFormationAndClubLimit(), PlayerDto::getValue, false).getPlayers();
        List<String> names = players.stream().map(PlayerDto::getWebName).collect(Collectors.toList());
        assertThat(players.size()).isEqualTo(11);

        assertThat(names).contains("Pope", "Zinchenko", "Ake", "Doherty", "Alonso", "Robertson", "Mount", "Klich", "Salah", "De Bruyne", "Jimenez");
    }

    @Test
    void dreamTeam_dontAllow253formation() {

        // given
        int budget = 15;

        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, getDataFor_253_formationBug(), PlayerDto::getValue, false);
        List<String> playerNames = dreamTeam.getPlayers().stream().map(PlayerDto::getWebName).collect(Collectors.toList());

        assertThat(dreamTeam.getFormation()).isNotEqualTo(253);
        assertThat(dreamTeam.getFormation()).isEqualTo(352);
        assertThat(playerNames).contains("Alonso");
        assertThat(playerNames).doesNotContain("Martial");
    }

    private List<PlayerDto> getPlayersStub() {
        return Arrays.asList(
                playerDto("Sterling", 10.0, 60.0, "MID", "A"),
                playerDto("De Bruyne", 20.0, 100.0, "MID", "A"),
                playerDto("Salah", 30.0, 120.0, "MID", "A"),
                playerDto("Grosicki", 10.0, 1.0, "MID", "B"),
                playerDto("Klich", 30.0, 1.0, "MID", "B"));
    }

    private List<PlayerDto> getDataFor_253_formationBug() {
        return Arrays.asList(
                playerDto("Pope", 1, 1000, "GKP", "A"),
                playerDto("Robertson", 1, 1000, "DEF", "A"),
                playerDto("Van Dijk", 1, 1000, "DEF", "A"),
                playerDto("Alonso", 1, 1, "DEF", "B"),
                playerDto("Sterling", 1, 1000, "MID", "B"),
                playerDto("De Bruyne", 1, 1000, "MID", "B"),
                playerDto("Salah", 1, 1000, "MID", "C"),
                playerDto("Grosicki", 1, 1000, "MID", "C"),
                playerDto("Klich", 1, 1000, "MID", "C"),
                playerDto("Aguero", 1, 1000, "FWD", "D"),
                playerDto("Vardy", 1, 1000, "FWD", "D"),
                playerDto("Martial", 1, 900, "FWD", "D"));
    }

    private List<PlayerDto> getPlayersStubRespectFormationAndClubLimit() {
        return Arrays.asList(
                playerDto("Pope", 1, 1002, "GKP", "A"),
                playerDto("Dubravka", 1, 1001, "GKP", "A"),

                playerDto("Zinchenko", 1, 505, "DEF", "A"),
                playerDto("Ake", 1, 504, "DEF", "B"),
                playerDto("Doherty", 1, 503, "DEF", "B"),
                playerDto("Alonso", 1, 502, "DEF", "B"),
                playerDto("Van Dijk", 1, 501, "DEF", "B"),
                playerDto("Robertson", 1, 500, "DEF", "C"),

                playerDto("Mount", 1, 106, "MID", "C"),
                playerDto("Klich", 1, 105, "MID", "C"),
                playerDto("Grosicki", 1, 104, "MID", "C"),
                playerDto("Salah", 1, 103, "MID", "D"),
                playerDto("De Bruyne", 1, 102, "MID", "D"),
                playerDto("Sterling", 1, 101, "MID", "D"),

                playerDto("Ings", 1, 5, "FWD", "C"),
                playerDto("Jimenez", 1, 4, "FWD", "D"),
                playerDto("Martial", 1, 3, "FWD", "D"),
                playerDto("Vardy", 1, 2, "FWD", "D"),
                playerDto("Aguero", 1, 1, "FWD", "D"));
    }

    private PlayerDto playerDto(String webName, double price, double value, String position, String club) {
        PlayerDto playerDto = mock(PlayerDto.class);
        when(playerDto.getWebName()).thenReturn(webName);
        when(playerDto.getCost()).thenReturn(price);
        when(playerDto.getValue()).thenReturn(value);
        when(playerDto.getPosition()).thenReturn(position);
        when(playerDto.getClub()).thenReturn(club);
        return playerDto;
    }
}
