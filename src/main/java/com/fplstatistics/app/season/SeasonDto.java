package com.fplstatistics.app.season;

import com.fplstatistics.app.round.RoundDto;

import java.util.List;

public class SeasonDto {

    private final String code;
    private final List<RoundDto> rounds;
    private final boolean active;

    SeasonDto(String code, List<RoundDto> rounds, boolean active) {
        this.code = code;
        this.rounds = rounds;
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public List<RoundDto> getRounds() {
        return rounds;
    }

    public boolean isActive() {
        return active;
    }
}
