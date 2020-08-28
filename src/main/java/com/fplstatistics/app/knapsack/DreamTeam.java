package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.position.Position;

import java.util.Comparator;
import java.util.List;

public class DreamTeam {

    private final List<PlayerDto> players;

    public DreamTeam(List<PlayerDto> players) {
        players.sort(Comparator.comparingInt(o -> Position.valueOf(o.getPosition()).getCode()));
        this.players = players;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public double getTeamPrice() {
        return players.stream().mapToDouble(PlayerDto::getCost).sum();
    }

    public double getTotalPoints() {
        return players.stream().mapToDouble(PlayerDto::getPoints).sum();
    }

    public double getTeamValue() {
        return players.stream().mapToDouble(PlayerDto::getValue).sum();
    }
}
