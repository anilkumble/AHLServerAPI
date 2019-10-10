package com.ahl.server.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "goals")
public class Goal {
    @Id
    private ObjectId goalId;
    private ObjectId matchId;
    private ObjectId forTeam;
    private ObjectId againstTeam;
    private ObjectId playerId;
    private ObjectId leagueId;

    public ObjectId getGoalId() {
        return goalId;
    }

    public void setGoalId(ObjectId goalId) {
        this.goalId = goalId;
    }

    public ObjectId getMatchId() {
        return matchId;
    }

    public void setMatchId(ObjectId matchId) {
        this.matchId = matchId;
    }

    public ObjectId getForTeam() {
        return forTeam;
    }

    public void setForTeam(ObjectId forTeam) {
        this.forTeam = forTeam;
    }

    public ObjectId getAgainstTeam() {
        return againstTeam;
    }

    public void setAgainstTeam(ObjectId againstTeam) {
        this.againstTeam = againstTeam;
    }

    public ObjectId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(ObjectId playerId) {
        this.playerId = playerId;
    }

    public ObjectId getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(ObjectId leagueId) {
        this.leagueId = leagueId;
    }
}
