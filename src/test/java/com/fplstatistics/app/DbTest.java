package com.fplstatistics.app;

import com.fplstatistics.app.knapsack.DreamTeam;
import com.fplstatistics.app.knapsack.DreamTeamService;
import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DbTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private DreamTeamService dreamTeamService;

    @Test
    void maxPoints_gw38() {

        // given
        int gameWeek = 38;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints);

        // then
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(131);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(83.0);
    }

    @Test
    void maxPoints_gw35() {

        // given
        int gameWeek = 35;
        int budget = 83;

        // when
        List<PlayerDto> players = getPlayers(gameWeek);
        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, PlayerDto::getPoints);

        // then
        assertThat(dreamTeam.getTotalPoints()).isEqualTo(151);
        assertThat(dreamTeam.getTeamPrice()).isEqualTo(71.5);
    }

    private List<PlayerDto> getPlayers(int round) {
        return playerService.getPlayers("2019-20", "2019-20", round, round, new ArrayList<>(), null, "90-100");
    }
}
