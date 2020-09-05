package com.fplstatistics.app.player;

import com.fplstatistics.app.round.RoundScore;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/shirts/").build()
                + Shirt.getShirtByTeamShortName(getClub()).getShirtUrl();
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

    public long getMinutesPerAppearance() {
        if (getAppearances() == 0) {
            return 0;
        } else {
            return roundScores.stream().mapToInt(RoundScore::getMinutes).sum() / getAppearances();
        }
    }

    public double getPointsPerAppearance() {
        if (getAppearances() == 0) {
            return 0;
        } else {
            return (double) getPoints() / getAppearances();
        }
    }

    public double getValue() {
        return getPoints() / player.getCurrentPrice();
    }

    public double getValuePerAppearance() {
        if (getAppearances() == 0) {
            return 0;
        } else {
            return getPoints() / player.getCurrentPrice() / getAppearances();
        }
    }

    @Override public String toString() {
        return "PlayerDto{" +
                "player=" + player +
                '}';
    }
}
