package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.position.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DreamTeam {

    private final List<PlayerDto> players;

    public DreamTeam(List<PlayerDto> players) {
        players.sort(Comparator.comparingInt(o -> Position.valueOf(o.getPosition()).getCode()));
        this.players = players;
    }

    public DreamTeam(List<PlayerDto> goalkeepers, List<PlayerDto> defenders, List<PlayerDto> midfielders, List<PlayerDto> forwards) {
        players = new ArrayList<>();
        players.addAll(goalkeepers);
        players.addAll(defenders);
        players.addAll(midfielders);
        players.addAll(forwards);
        players.sort(Comparator.comparingInt(o -> Position.valueOf(o.getPosition()).getCode()));
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public int getFormation() {
        return Integer.parseInt("" + getDefenders().size() + getMidfielders().size() + getForwards().size());
    }

    public List<PlayerDto> getGoalkeepers() {
        return getPlayersByPosition(Position.GKP.name());
    }

    public List<PlayerDto> getDefenders() {
        return getPlayersByPosition(Position.DEF.name());
    }

    public List<PlayerDto> getMidfielders() {
        return getPlayersByPosition(Position.MID.name());
    }

    public List<PlayerDto> getForwards() {
        return getPlayersByPosition(Position.FWD.name());
    }

    private List<PlayerDto> getPlayersByPosition(String position) {
        return players.stream().filter(p -> p.getPosition().equals(position)).collect(Collectors.toList());
    }

    public double getTeamPrice() {
        return players.stream().mapToDouble(PlayerDto::getCost).sum();
    }

    public int getTotalPoints() {
        return players.stream().mapToInt(PlayerDto::getPoints).sum();
    }

    public double getTeamValue() {
        return players.stream().mapToDouble(PlayerDto::getValue).sum();
    }

    @Override
    public String toString() {
        return "DreamTeam{" +
                "players=" + players +
                '}';
    }
}
