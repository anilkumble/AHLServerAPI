package com.ahl.server.repository;

import com.ahl.server.entity.Goal;
import com.ahl.server.entity.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoalRepository extends CrudRepository<Goal, ObjectId> {

    public Goal findFirstById(ObjectId goalId);

    @Query(value = "{ 'playerId' : ?0}")
    public List<Goal> findAllGoalsByplayerId(ObjectId id);

    @Query(value = "{'forTeamId' : ?0}")
    public List<Goal> findGoalsScoredByTeamId(ObjectId TeamId);

    @Query(value = "{'againstTeamId' : ?0}")
    public List<Goal> findGoalsAgainstByTeamId(ObjectId TeamId);
}


