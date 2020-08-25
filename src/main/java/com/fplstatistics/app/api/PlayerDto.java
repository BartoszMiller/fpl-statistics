package com.fplstatistics.app.api;

import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.model.RoundScore;
import com.fplstatistics.app.model.Shirt;

import java.util.List;

public class PlayerDto {

    private final Player player;
    private final List<RoundScore> roundScores;

    public PlayerDto(Player player, List<RoundScore> roundScores) {
        this.player = player;
        this.roundScores = roundScores;
    }

    public String getWebName() {
        return player.getWebName();
    }

    public String getClub() {
        return player.getCurrentTeam().getShortName();
    }

    public String getShirtUrl() {
        return Shirt.getShirtByTeamShortName(getClub()).getShirtUrl();
    }

    public String getPosition() {
        return player.getCurrentPosition();
    }

    public String getCost() {
        return String.format("%.1f", player.getCurrentPrice());
    }

    public int getPoints() {
        return roundScores.stream().mapToInt(RoundScore::getPoints).sum();
    }

    public long getAppearances() {
        return roundScores.stream().filter(roundScore -> roundScore.getMinutes() > 0).count();
    }

    public long getMinutesPerAppearance() {
        if (getAppearances() == 0) {
            return 0;
        } else {
            return roundScores.stream().mapToInt(RoundScore::getMinutes).sum() / getAppearances();
        }
    }

    public String getPointsPerAppearance() {
        if (getAppearances() == 0) {
            return "0";
        } else {
            return String.format("%.2f", (double) getPoints() / getAppearances());
        }
    }

    public String getValue() {
        return String.format("%.2f", getPoints() / player.getCurrentPrice());
    }

    public String getValuePerAppearance() {
        if (getAppearances() == 0) {
            return "0";
        } else {
            return String.format("%.2f", getPoints() / player.getCurrentPrice() / getAppearances());
        }
    }
}
