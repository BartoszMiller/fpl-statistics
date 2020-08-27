package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;

import java.util.List;

public class DreamTeam {

    private final List<PlayerDto> players;

    public DreamTeam(List<PlayerDto> players) {
        this.players = players;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public double getTeamPrice() {
        return players.stream().mapToDouble(PlayerDto::getCost).sum();
    }
}
