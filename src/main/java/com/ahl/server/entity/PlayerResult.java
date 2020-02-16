package com.ahl.server.entity;

import java.util.List;
import java.util.StringJoiner;

public class PlayerResult {

    Player player;
    int goalScored;
    List<Card> cards;

    public PlayerResult(Player player, int goalScored, List<Card> cards) {
        this.player = player;
        this.goalScored = goalScored;
        this.cards = cards;
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlayerResult.class.getSimpleName() + "[", "]")
                .add("player=" + player)
                .add("goalScored=" + goalScored)
                .add("cards=" + cards)
                .toString();
    }
}
