package com.fplstatistics.app.model;

import java.util.Arrays;

public enum Position {

    GKP(1),
    DEF(2),
    MID(3),
    FWD(4);

    private final int code;

    Position(int code) {
        this.code = code;
    }

    public static Position getPositionByCode(int code) {
        return Arrays.stream(Position.values())
                .filter(position -> position.code == code)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No position for code " + code));
    }

}
