package com.ahl.server.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "points")
public class Points {

    @Id
    private ObjectId id;
    private ObjectId teamId;
    private int points;
    private int goalScored;
    private int goalAgainst;
    private int matchesPlayed;
    private int won;
    private int draw;
    private int lost;

    public Points(ObjectId teamId) {
        this.teamId = teamId;
    }

    public ObjectId getTeamId() {
        return teamId;
    }

    public void setTeamId(ObjectId teamId) {
        this.teamId = teamId;
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
}
