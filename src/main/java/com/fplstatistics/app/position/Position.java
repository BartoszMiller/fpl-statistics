package com.fplstatistics.app.position;

import java.util.Arrays;

public enum Position {

    GKP(1, "Goalkeeper"),
    DEF(2, "Defender"),
    MID(3, "Midfielder"),
    FWD(4, "Forward");

    private final int code;
    private final String positionName;

    Position(int code, String positionName) {
        this.code = code;
        this.positionName = positionName;
    }

    public String getName() {
        return this.name();
    }

    public int getCode() {
        return code;
    }

    public String getPositionName() {
        return positionName;
    }

    public static Position getPositionByCode(int code) {
        return Arrays.stream(Position.values())
                .filter(position -> position.code == code)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No position for code " + code));
    }

}
