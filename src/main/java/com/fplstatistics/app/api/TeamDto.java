package com.fplstatistics.app.api;

public class TeamDto {

    private String name;
    private String shortName;

    public TeamDto(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
