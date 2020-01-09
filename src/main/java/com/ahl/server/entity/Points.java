package com.ahl.server.entity;

import java.util.StringJoiner;

public class Points {

    private Team team;
    private int position;
    private int points;
    private int goalScored;
    private int goalAgainst;
    private int goalDifference;
    private int matchesPlayed;
    private int won;
    private int draw;
    private int lost;

    public Points() {
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalScored() {
        return goalScored;
    }

    public void setGoalScored(int goalScored) {
        this.goalScored = goalScored;
    }

    public int getGoalAgainst() {
        return goalAgainst;
    }

    public void setGoalAgainst(int goalAgainst) {
        this.goalAgainst = goalAgainst;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public void increaseMP(){
        this.matchesPlayed++;
    }

    public void increaseWon(){
        this.won++;
        this.points +=3;
    }

    public void increaseDraw(){
        this.draw++;
        this.points+=1;
    }

    public void increaseLost(){
        this.lost++;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Points.class.getSimpleName() + "[", "]")
                .add("team=" + team)
                .add("position=" + position)
                .add("points=" + points)
                .add("goalScored=" + goalScored)
                .add("goalAgainst=" + goalAgainst)
                .add("goalDifference=" + goalDifference)
                .add("matchesPlayed=" + matchesPlayed)
                .add("won=" + won)
                .add("draw=" + draw)
                .add("lost=" + lost)
                .toString();
    }
}
