package com.fplstatistics.app.season;

public class SeasonDto {

    private final String code;
    private final boolean active;

    public SeasonDto(String code, boolean active) {
        this.code = code;
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return active;
    }
}
