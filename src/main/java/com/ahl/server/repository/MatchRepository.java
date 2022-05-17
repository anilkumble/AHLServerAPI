package com.ahl.server.repository;

import com.ahl.server.entity.Match;
import com.ahl.server.enums.MatchStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match,ObjectId> {

    public Match findFirstById(ObjectId matchId);

    @Query(value = "{'tournamentId' : ?0}")
    public Iterable<Match> findAllMatchByTournament(ObjectId tournamentId);

    @Query(value = "{ 'tournamentId' : ?0,  'status' : ?1}")
    public Iterable<Match> findCompletedMatch(ObjectId tournamentId, MatchStatus matchStatus);

}
