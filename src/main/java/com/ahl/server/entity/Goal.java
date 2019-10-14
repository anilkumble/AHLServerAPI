package com.ahl.server.entity;

import com.ahl.server.AHLConstants;
import com.ahl.server.exception.InSufficientDataException;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "goals")
public class Goal {
    @Id
    private ObjectId id;
    private ObjectId matchId;
    private ObjectId forTeamId;
    private ObjectId againstTeamId;
    private ObjectId playerId;

    public ObjectId getAgainstTeamId() {
        return againstTeamId;
    }

    public void setAgainstTeamId(ObjectId againstTeamId) {
        this.againstTeamId = againstTeamId;
    }

    public static boolean validateGoal(Goal goal) {
        if(!ObjectUtils.allNotNull(goal.getForTeamId(),goal.getMatchId(),goal.getPlayerId()) || ObjectUtils.isEmpty(goal.getForTeamId())
        || ObjectUtils.isEmpty(goal.getPlayerId()) ||  ObjectUtils.isEmpty(goal.getMatchId()))
        {
            return false;
        }
        return true;
    }


    public ObjectId getForTeamId() {
        return forTeamId;
    }

    public void setForTeamId(ObjectId forTeamId) {
        this.forTeamId = forTeamId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getMatchId() {
        return matchId;
    }

    public void setMatchId(ObjectId matchId) {
        this.matchId = matchId;
    }


    public ObjectId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(ObjectId playerId) {
        this.playerId = playerId;
    }

}
