package com.ahl.server.repository;

import com.ahl.server.entity.PlayerTeamRelation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerTeamRepository extends CrudRepository<PlayerTeamRelation, ObjectId> {

    public PlayerTeamRepository findFirstById(ObjectId id);

    public List<PlayerTeamRelation> findAllRelationByPlayerId(ObjectId playerId);

    public List<PlayerTeamRelation> findAllRelationByTeamId(ObjectId teamId);

    @Query(value = "{ 'teamId' : ?0, 'playerId' : ?1 }")
    List<PlayerTeamRelation> isPlayerRelationExistInTeam(ObjectId teamId, ObjectId playerID);

}
