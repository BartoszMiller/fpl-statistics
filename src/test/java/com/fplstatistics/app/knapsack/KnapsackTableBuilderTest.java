package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KnapsackTableBuilderTest {

    private final KnapsackTableBuilder builder = new KnapsackTableBuilder();

    @ParameterizedTest
    @MethodSource("comparators")
    void exampleFromYoutube_zeroBudget(Comparator<PlayerDto> comparator) {

        // given
        List<PlayerDto> players = getSimplePlayers();
        players.sort(comparator);

        // when
        int[][][] table = builder.buildTable(0, players, 2, PlayerDto::getValue);
        List<String> mapToNames = mapToNames(builder.getPlayersForBestCell(table, players, PlayerDto::getValue));

        // then
        assertThat(table[table.length - 1][table[0].length - 1][table[0][0].length - 1]).isEqualTo(0);
        assertThat(mapToNames).containsExactlyInAnyOrder();
    }

    @ParameterizedTest
    @MethodSource("comparators")
    void exampleFromYoutube_canAffordTwo_noPlayersCount(Comparator<PlayerDto> comparator) {

        // given
        List<PlayerDto> players = getSimplePlayers();
        players.sort(comparator);

        // when
        int[][][] table = builder.buildTable(5, players, 2, PlayerDto::getValue);
        List<String> mapToNames = mapToNames(builder.getPlayersForBestCell(table, players, PlayerDto::getValue));

        // then
        assertThat(table[table.length - 1][table[0].length - 1][table[0][0].length - 1]).isEqualTo(800);
        assertThat(mapToNames).containsExactlyInAnyOrder("Grosicki", "De Bruyne");
    }

    @ParameterizedTest
    @MethodSource("comparators")
    void exampleFromYoutube_canAffordAll_noPlayersCount(Comparator<PlayerDto> comparator) {

        // given
        List<PlayerDto> players = getSimplePlayers();
        players.sort(comparator);

        // when
        int[][][] table = builder.buildTable(14, players, 4, PlayerDto::getValue);
        List<String> mapToNames = mapToNames(builder.getPlayersForBestCell(table, players, PlayerDto::getValue));

        // then
        assertThat(table[table.length - 1][table[0].length - 1][table[0][0].length - 1]).isEqualTo(2100);
        assertThat(mapToNames).containsExactlyInAnyOrder("Sterling", "Salah", "Grosicki", "De Bruyne");
    }

    @ParameterizedTest
    @MethodSource("comparators")
    void exampleFromYoutube_canAffordTwo_2players(Comparator<PlayerDto> comparator) {

        // given
        List<PlayerDto> players = getSimplePlayers();
        players.sort(comparator);

        // when
        int[][][] table = builder.buildTable(5, players, 2, PlayerDto::getValue);
        List<String> mapToNames = mapToNames(builder.getPlayersForBestCell(table, players, PlayerDto::getValue));

        // then
        assertThat(table[table.length - 1][table[0].length - 1][table[0][0].length - 1]).isEqualTo(800);
        assertThat(mapToNames).containsExactlyInAnyOrder("Grosicki", "De Bruyne");
    }

    @ParameterizedTest
    @MethodSource("comparators")
    void exampleFromYoutube_canAffordTwo_4players(Comparator<PlayerDto> comparator) {

        // given
        List<PlayerDto> players = getSimplePlayers();
        players.sort(comparator);

        // when
        int[][][] table = builder.buildTable(5, players, 4, PlayerDto::getValue);
        List<String> mapToNames = mapToNames(builder.getPlayersForBestCell(table, players, PlayerDto::getValue));

        // then
        assertThat(table[table.length - 1][table[0].length - 1][table[0][0].length - 1]).isEqualTo(800);
        assertThat(mapToNames).containsExactlyInAnyOrder("Grosicki", "De Bruyne");
    }

    @ParameterizedTest
    @MethodSource("comparators")
    void exampleFromYoutube_canAffordAll_2players(Comparator<PlayerDto> comparator) {

        // given
        List<PlayerDto> players = getSimplePlayers();
        players.sort(comparator);

        // when
        int[][][] table = builder.buildTable(14, players, 2, PlayerDto::getValue);
        List<String> mapToNames = mapToNames(builder.getPlayersForBestCell(table, players, PlayerDto::getValue));

        // then
        assertThat(table[table.length - 1][table[0].length - 1][table[0][0].length - 1]).isEqualTo(1300);
        assertThat(mapToNames).containsExactlyInAnyOrder("Salah", "Sterling");
    }

    @ParameterizedTest
    @MethodSource("comparators")
    void exampleFromYoutube_canAffordAll_4players(Comparator<PlayerDto> comparator) {

        // given
        List<PlayerDto> players = getSimplePlayers();
        players.sort(comparator);

        // when
        int[][][] table = builder.buildTable(14, players, 4, PlayerDto::getValue);
        List<String> mapToNames = mapToNames(builder.getPlayersForBestCell(table, players, PlayerDto::getValue));

        // then
        assertThat(table[table.length - 1][table[0].length - 1][table[0][0].length - 1]).isEqualTo(2100);
        assertThat(mapToNames).containsExactlyInAnyOrder("Sterling", "Salah", "Grosicki", "De Bruyne");
    }

    private static List<PlayerDto> getSimplePlayers() {
        return Arrays.asList(
                playerDto("Sterling", 5, 6, "MID", "A"),
                playerDto("De Bruyne", 3, 5, "MID", "A"),
                playerDto("Salah", 4, 7, "MID", "A"),
                playerDto("Grosicki", 2, 3, "MID", "B")
        );
    }

    private static Stream<Arguments> comparators() {
        return Stream.of(
                Arguments.of(Comparator.comparingDouble(PlayerDto::getValue)),
                Arguments.of(Comparator.comparingDouble(PlayerDto::getValue).reversed()),
                Arguments.of(Comparator.comparingDouble(PlayerDto::getPoints)),
                Arguments.of(Comparator.comparingDouble(PlayerDto::getPoints).reversed()),
                Arguments.of(Comparator.comparingDouble(PlayerDto::getCost)),
                Arguments.of(Comparator.comparingDouble(PlayerDto::getCost).reversed()),
                Arguments.of(Comparator.comparing(PlayerDto::getWebName)),
                Arguments.of(Comparator.comparing(PlayerDto::getWebName).reversed())
        );
    }

    private static PlayerDto playerDto(String webName, double weight, double value, String position, String club) {
        PlayerDto playerDto = mock(PlayerDto.class);
        when(playerDto.getWebName()).thenReturn(webName);
        when(playerDto.getCost()).thenReturn(weight);
        when(playerDto.getValue()).thenReturn(value);
        when(playerDto.getPosition()).thenReturn(position);
        when(playerDto.getClub()).thenReturn(club);
        return playerDto;
    }

    private List<String> mapToNames(List<PlayerDto> players) {
        return players.stream().map(PlayerDto::getWebName).collect(Collectors.toList());
    }
}
