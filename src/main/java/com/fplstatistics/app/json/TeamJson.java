package com.fplstatistics.app.json;

public class TeamJson implements Comparable<TeamJson> {

    public int code;
    public int draw;
    public Object form;
    public int id;
    public int loss;
    public String name;
    public int played;
    public int points;
    public int position;
    public String short_name;
    public int strength;
    public Object team_division;
    public boolean unavailable;
    public int win;
    public int strength_overall_home;
    public int strength_overall_away;
    public int strength_attack_home;
    public int strength_attack_away;
    public int strength_defence_home;
    public int strength_defence_away;
    public int pulse_id;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public Object getForm() {
        return form;
    }

    public void setForm(Object form) {
        this.form = form;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public Object getTeam_division() {
        return team_division;
    }

    public void setTeam_division(Object team_division) {
        this.team_division = team_division;
    }

    public boolean isUnavailable() {
        return unavailable;
    }

    public void setUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getStrength_overall_home() {
        return strength_overall_home;
    }

    public void setStrength_overall_home(int strength_overall_home) {
        this.strength_overall_home = strength_overall_home;
    }

    public int getStrength_overall_away() {
        return strength_overall_away;
    }

    public void setStrength_overall_away(int strength_overall_away) {
        this.strength_overall_away = strength_overall_away;
    }

    public int getStrength_attack_home() {
        return strength_attack_home;
    }

    public void setStrength_attack_home(int strength_attack_home) {
        this.strength_attack_home = strength_attack_home;
    }

    public int getStrength_attack_away() {
        return strength_attack_away;
    }

    public void setStrength_attack_away(int strength_attack_away) {
        this.strength_attack_away = strength_attack_away;
    }

    public int getStrength_defence_home() {
        return strength_defence_home;
    }

    public void setStrength_defence_home(int strength_defence_home) {
        this.strength_defence_home = strength_defence_home;
    }

    public int getStrength_defence_away() {
        return strength_defence_away;
    }

    public void setStrength_defence_away(int strength_defence_away) {
        this.strength_defence_away = strength_defence_away;
    }

    public int getPulse_id() {
        return pulse_id;
    }

    public void setPulse_id(int pulse_id) {
        this.pulse_id = pulse_id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(TeamJson o) {
        return this.getName().compareTo(o.getName());
    }
}
