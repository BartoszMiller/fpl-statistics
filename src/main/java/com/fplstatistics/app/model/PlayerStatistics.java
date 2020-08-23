package com.fplstatistics.app.model;

public class PlayerStatistics {

    private String shirt;
    private String lastName;
    private String club;
    private String position;
    private String cost;
    private double points;
    private double appearances;
    private double minutesPerAppearances;
    private double pointsPerAppearances;
    private double value;
    private double valuePerAppearances;

    public String getShirt() {
        return shirt;
    }

    public void setShirt(String shirt) {
        this.shirt = shirt;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getAppearances() {
        return appearances;
    }

    public void setAppearances(double appearances) {
        this.appearances = appearances;
    }

    public double getMinutesPerAppearances() {
        return minutesPerAppearances;
    }

    public void setMinutesPerAppearances(double minutesPerAppearances) {
        this.minutesPerAppearances = minutesPerAppearances;
    }

    public double getPointsPerAppearances() {
        return pointsPerAppearances;
    }

    public void setPointsPerAppearances(double pointsPerAppearances) {
        this.pointsPerAppearances = pointsPerAppearances;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValuePerAppearances() {
        return valuePerAppearances;
    }

    public void setValuePerAppearances(double valuePerAppearances) {
        this.valuePerAppearances = valuePerAppearances;
    }
}
