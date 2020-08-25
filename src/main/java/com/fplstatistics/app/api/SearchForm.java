package com.fplstatistics.app.api;

import com.fplstatistics.app.model.Position;

public class SearchForm {

    private String seasonFrom;
    private Integer roundFrom;
    private String seasonTo;
    private Integer roundTo;
    private String teamShortName;
    private Position position;

    public String getSeasonFrom() {
        return seasonFrom;
    }

    public void setSeasonFrom(String seasonFrom) {
        this.seasonFrom = seasonFrom;
    }

    public Integer getRoundFrom() {
        return roundFrom;
    }

    public void setRoundFrom(Integer roundFrom) {
        this.roundFrom = roundFrom;
    }

    public String getSeasonTo() {
        return seasonTo;
    }

    public void setSeasonTo(String seasonTo) {
        this.seasonTo = seasonTo;
    }

    public Integer getRoundTo() {
        return roundTo;
    }

    public void setRoundTo(Integer roundTo) {
        this.roundTo = roundTo;
    }

    public String getTeamShortName() {
        return teamShortName;
    }

    public void setTeamShortName(String teamShortName) {
        this.teamShortName = teamShortName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
