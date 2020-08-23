package com.fplstatistics.app.model;

public enum Position {

    GKP("Goalkeeper"),
    DEF("Defender"),
    MID("Midfielder"),
    FWD("Forward");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
