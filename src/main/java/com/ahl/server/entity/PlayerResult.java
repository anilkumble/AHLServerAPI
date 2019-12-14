package com.ahl.server.entity;

import java.util.StringJoiner;

public class PlayerResult {

    Player player;
    int goalScored;
    int greenCard;
    int yellowCard;
    int redCard;

    public PlayerResult(Player player, int goalScored, int greenCard, int yellowCard, int redCard) {
        this.player = player;
        this.goalScored = goalScored;
        this.greenCard = greenCard;
        this.yellowCard = yellowCard;
        this.redCard = redCard;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getGoalScored() {
        return goalScored;
    }

    public void setGoalScored(int goalScored) {
        this.goalScored = goalScored;
    }

    public int getGreenCard() {
        return greenCard;
    }

    public void setGreenCard(int greenCard) {
        this.greenCard = greenCard;
    }

    public int getYellowCard() {
        return yellowCard;
    }

    public void setYellowCard(int yellowCard) {
        this.yellowCard = yellowCard;
    }

    public int getRedCard() {
        return redCard;
    }

    public void setRedCard(int redCard) {
        this.redCard = redCard;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlayerResult.class.getSimpleName() + "[", "]")
                .add("player=" + player)
                .add("goalScored=" + goalScored)
                .add("greenCard=" + greenCard)
                .add("yellowCard=" + yellowCard)
                .add("redCard=" + redCard)
                .toString();
    }
}
