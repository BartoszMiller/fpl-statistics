package com.fplstatistics.app.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DataJson {

    private List<TeamJson> teams = new ArrayList<>();
    private List<PlayerJson> players = new ArrayList<>();

    public List<TeamJson> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamJson> teams) {
        this.teams = teams;
    }

    public List<PlayerJson> getPlayers() {
        return new ArrayList<>(players);
    }

    @JsonProperty("elements")
    public void setPlayers(List<PlayerJson> players) {
        this.players = players;
    }

    public void setTeamsOnPlayers() {
        players.forEach(playerJson -> {
            TeamJson teamJson = teams.stream().filter(t -> t.getCode() == playerJson.getTeam_code()).findFirst().get();
            playerJson.setTeamJsonObject(teamJson);
        });
    }

    @JsonIgnore
    public Map<TeamJson, Set<PlayerJson>> getTeamToPlayers() {
        Map<TeamJson, Set<PlayerJson>> teamToPlayers = new TreeMap<>();
        teams.forEach(teamJson -> teamToPlayers.put(teamJson, new TreeSet<>()));
        players.forEach(playerJson -> {
            TeamJson teamJson = teams.stream().filter(t -> t.getCode() == playerJson.getTeam_code()).findFirst().get();
            teamToPlayers.get(teamJson).add(playerJson);
        });
        return teamToPlayers;
    }
}
