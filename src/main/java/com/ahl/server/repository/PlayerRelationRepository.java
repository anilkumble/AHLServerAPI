package com.ahl.server.repository;

import com.ahl.server.entity.PlayerRelation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PlayerRelationRepository extends MongoRepository<PlayerRelation, ObjectId> {

    @Query("{'teamId' :?0}")
    public List<PlayerRelation> findAllPlayerinaTeam(ObjectId teamid);


    @Query(value = "{ 'teamId' : ?0, 'playerId' : ?1 }")
    List<PlayerRelation> isPlayerRelationExistInTeam(ObjectId teamId, ObjectId playerID);
}
