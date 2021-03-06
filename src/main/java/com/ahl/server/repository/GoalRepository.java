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

    @Query(value = "{ 'tournamentId': ?0, 'playerId' : ?1}")
    public List<Goal> findFirstByIdAndTournamentId(ObjectId tournamentId, ObjectId playerId);

    @Query(value = "{ 'playerId' : ?0}")
    public List<Goal> findAllGoalsByplayerId(ObjectId id);

    @Query(value = "{'forTeamId' : ?0}")
    public List<Goal> findAllGoalsScoredByforTeamId(ObjectId id);

    @Query(value = "{'againstTeamId' : ?0}")
    public List<Goal> findGoalsAgainstByTeamId(ObjectId TeamId);

    @Query(value= "{ 'matchId': ?0 , 'forTeamId':?1 }")
    public List<Goal> findGoalsByTeamInMatch(ObjectId matchId,ObjectId teamId);

}


