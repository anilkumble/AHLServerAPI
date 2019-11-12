package com.ahl.server.entity;

import com.ahl.server.enums.MatchStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "matches")
public class Match {
    @Id
    private ObjectId id;
    private ObjectId team1;
    private ObjectId team2;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date matchDateTime;
    private ObjectId tournamentId;
    private int result;
    private MatchStatus status;

    public Match(ObjectId team1, ObjectId team2, ObjectId tournamentId) {
        this.team1 = team1;
        this.team2 = team2;
        this.tournamentId = tournamentId;
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

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
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

    public static boolean validateMatch(Match match) {
        if(!ObjectUtils.allNotNull(match.getTeam1(),match.getTeam2(),match.getTournamentId()) || ObjectUtils.isEmpty(match.getTeam1())
                || ObjectUtils.isEmpty(match.getTeam2()) || ObjectUtils.isEmpty(match.getTournamentId()))
        {
            return false;
        }
        return true;
    }
}
