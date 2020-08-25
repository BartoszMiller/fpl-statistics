package com.fplstatistics.app.position;

public class PositionDto {

    private final int code;
    private final String name;

    public PositionDto(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
