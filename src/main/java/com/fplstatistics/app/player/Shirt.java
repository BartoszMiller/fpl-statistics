package com.fplstatistics.app.player;

import java.util.Arrays;

public enum Shirt {

    ARS("arsenal.jpg"),
    AVL("aston-villa.jpg"),
    BHA("brighton.jpg"),
    BUR("burnley.jpg"),
    CHE("chelsea.jpg"),
    CRY("crystal.jpg"),
    EVE("everton.jpg"),
    FUL("fulham.jpg"),
    LEI("leicester.jpg"),
    LEE("leeds.jpg"),
    LIV("liverpool.jpg"),
    MCI("man-city.jpg"),
    MUN("man-utd.jpg"),
    NEW("newcastle.jpg"),
    SHU("sheffield.jpg"),
    SOU("southampton.jpg"),
    TOT("spurs.jpg"),
    WBA("west-brom.jpg"),
    WHU("west-ham.jpg"),
    WOL("wolves.jpg");

    private final String shirtUrl;

    Shirt(String shirtUrl) {
        this.shirtUrl = shirtUrl;
    }

    public String getShirtUrl() {
        return shirtUrl;
    }

    public static Shirt getShirtByTeamShortName(String teamShortName) {
        return Arrays.stream(Shirt.values())
                .filter(a -> a.name().equals(teamShortName)).findFirst()
                .orElseThrow(() -> new RuntimeException("No shirt for team " + teamShortName));
    }
}
