package com.ahl.server.entity;

import com.ahl.server.AHLConstants;
import com.ahl.server.exception.InSufficientDataException;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "playerrelations")
public class PlayerTeamRelation {

    @Id
    ObjectId id;
    ObjectId playerId;
    ObjectId teamId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(ObjectId playerId) {
        this.playerId = playerId;
    }

    public ObjectId getTeamId() {
        return teamId;
    }

    public void setTeamId(ObjectId teamId) {
        this.teamId = teamId;
    }

    public static void validatePlayerTeam(PlayerTeamRelation ptRelation) throws InSufficientDataException {

        if(! ObjectUtils.allNotNull(ptRelation.getPlayerId(), ptRelation.getTeamId()) ||
                ObjectUtils.isEmpty(ptRelation.getPlayerId()) || ObjectUtils.isEmpty(ptRelation.getTeamId())) {
            Map<String, String> substitueMap = new HashMap<>();
            substitueMap.put("fields", Arrays.toString(new Object[]{"teamId", "playerId"}));
            throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS, substitueMap);
        }
    }
}
