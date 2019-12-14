package com.ahl.server.repository;

import com.ahl.server.entity.Team;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, ObjectId> {

    public Team findFirstById(ObjectId id);

    @Query(value = "{'tournamentId' : ?0}")
    public List<Team> findTeamsByTournament(ObjectId tournamentId);

}


