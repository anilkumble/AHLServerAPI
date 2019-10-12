package com.ahl.server.entity;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matches")
public class Match {
    @Id
    private ObjectId id;
    private ObjectId team1;
    private ObjectId team2;
    private ObjectId tournamentId;
    private int result;

    public Match(ObjectId team1, ObjectId team2, ObjectId tournamentId) {
        this.team1 = team1;
        this.team2 = team2;
        this.tournamentId = tournamentId;
    }

    public static boolean validateMatch(Match match) {
        if(!ObjectUtils.allNotNull(match.getTeam1(),match.getTeam2(),match.getTournamentId()) || ObjectUtils.isEmpty(match.getTeam1())
        || ObjectUtils.isEmpty(match.getTeam2()) || ObjectUtils.isEmpty(match.getTournamentId()))
        {
            return false;
        }
        return true;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getTeam1() {
        return team1;
    }

    public void setTeam1(ObjectId team1) {
        this.team1 = team1;
    }

    public ObjectId getTeam2() {
        return team2;
    }

    public void setTeam2(ObjectId team2) {
        this.team2 = team2;
    }

    public ObjectId getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(ObjectId tournamentId) {
        this.tournamentId = tournamentId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
