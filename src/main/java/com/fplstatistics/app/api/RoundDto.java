package com.fplstatistics.app.api;

public class RoundDto {

    private final int round;
    private final String label;

    public RoundDto(int round, String label) {
        this.round = round;
        this.label = label;
    }

    public int getRound() {
        return round;
    }

    public String getLabel() {
        return label;
    }
}
