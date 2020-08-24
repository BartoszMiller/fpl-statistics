package com.fplstatistics.app.api;

import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.model.RoundScore;

import java.util.List;

public class PlayerDto {

    private final Player player;
    private final List<RoundScore> roundScores;
    private String shirtUrl;

    public PlayerDto(Player player, List<RoundScore> roundScores) {
        this.player = player;
        this.roundScores = roundScores;
    }

    public String getLastName() {
        return player.getLastName();
    }

    public String getClub() {
        return player.getCurrentTeam().getShortName();
    }

    public String getShirtUrl() {
        return shirtUrl;
    }

    public void setShirtUrl(String shirtUrl) {
        this.shirtUrl = shirtUrl;
    }

    public String getPosition() {
        return player.getCurrentPosition();
    }

    public double getCost() {
        return player.getCurrentPrice();
    }

    public int getPoints() {
        return roundScores.stream().mapToInt(RoundScore::getPoints).sum();
    }

    public long getAppearances() {
        return roundScores.stream().filter(roundScore -> roundScore.getMinutes() > 0).count();
    }

    public long getMinutesPerAppearence() {
        if (getAppearances() == 0) {
            return 0;
        } else {
            return roundScores.stream().mapToInt(RoundScore::getMinutes).sum() / getAppearances();
        }
    }

    public long getPointsPerAppearence() {
        if (getAppearances() == 0) {
            return 0;
        } else {
            return getPoints() / getAppearances();
        }
    }

    public double getValue() {
        return getPoints() / getCost();
    }

    public double getValuePerAppearence() {
        if (getAppearances() == 0) {
            return 0;
        } else {
            return getValue() / getAppearances();
        }
    }
}
