package com.fplstatistics.app;

import com.fplstatistics.app.knapsack.DreamTeam;
import com.fplstatistics.app.knapsack.DreamTeamService;
import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DbTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private DreamTeamService dreamTeamService;

    @Test
    void maxPoints_gw35_noBlackWhiteLists() {

        // given
        int gameWeek = 35;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, false);

        // then
        assertThat(dreamTeam.getFormation()).isEqualTo(433);
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(151);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(71.5);
    }

    @Test
    void maxPoints_gw35_withEmptyWhiteLists() {

        // given
        int gameWeek = 35;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, new ArrayList<>());

        // then
        assertThat(dreamTeam.getFormation()).isEqualTo(433);
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(151);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(71.5);
    }

    @Test
    void maxPoints_gw35_fullWhiteLists() {

        // given
        int gameWeek = 35;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, new ArrayList<>());
        DreamTeam teamFromWhiteList = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, dreamTeam.getPlayers());

        // then
        assertThat(teamFromWhiteList.getFormation()).isEqualTo(433);
        assertThat(teamFromWhiteList.getTotalPoints()).isEqualTo(151);
        assertThat(teamFromWhiteList.getTeamPrice()).isEqualTo(71.5);
    }

    @Test
    void maxPoints_gw35_whiteListsRandom() {

        // given
        int gameWeek = 35;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, new ArrayList<>());
        List<PlayerDto> whiteList = Arrays.asList(dreamTeam.getPlayers().get(0), dreamTeam.getPlayers().get(3), dreamTeam.getPlayers().get(5), dreamTeam.getPlayers().get(10));
        DreamTeam teamFromWhiteList = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, whiteList);

        // then
        assertThat(teamFromWhiteList.getFormation()).isEqualTo(433);
        assertThat(teamFromWhiteList.getTotalPoints()).isEqualTo(151);
        assertThat(teamFromWhiteList.getTeamPrice()).isEqualTo(71.5);
    }

    @Test
    void maxPoints_gw38_noBlackWhiteLists() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, false);

        // then
        assertThat(dreamTeam.getFormation()).isEqualTo(352);
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(131);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(83.0);
    }

    @Test
    void maxPoints_gw38_withEmptyWhiteLists() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, new ArrayList<>());

        // then
        assertThat(dreamTeam.getFormation()).isEqualTo(352);
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(131);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(83.0);
    }

    @Test
    void maxPoints_gw38_fullWhiteLists() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, new ArrayList<>());
        DreamTeam teamFromWhiteList = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, dreamTeam.getPlayers());

        // then
        assertThat(teamFromWhiteList.getFormation()).isEqualTo(352);
        assertThat(teamFromWhiteList.getTotalPoints()).isEqualTo(131);
        assertThat(teamFromWhiteList.getTeamPrice()).isEqualTo(83.0);
    }

    @Test
    void maxPoints_gw38_whiteListsRandom() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, new ArrayList<>());
        List<PlayerDto> whiteList = Arrays.asList(dreamTeam.getPlayers().get(0), dreamTeam.getPlayers().get(3), dreamTeam.getPlayers().get(5), dreamTeam.getPlayers().get(10));
        DreamTeam teamFromWhiteList = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints, whiteList);

        // then
        assertThat(teamFromWhiteList.getFormation()).isEqualTo(352);
        assertThat(teamFromWhiteList.getTotalPoints()).isEqualTo(131);
        assertThat(teamFromWhiteList.getTeamPrice()).isEqualTo(83.0);
    }

    private List<PlayerDto> getPlayers(int round) {
        return playerService.getPlayers("2019-20", "2019-20", round, round, new ArrayList<>(), null, ">90", true, true);
    }
}
