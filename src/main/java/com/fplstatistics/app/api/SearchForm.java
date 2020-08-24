package com.fplstatistics.app.api;

public class SearchForm {

    private String seasonFrom;
    private Integer roundFrom;
    private String seasonTo;
    private Integer roundTo;

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
}
