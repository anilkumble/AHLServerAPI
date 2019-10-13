package com.ahl.server.repository;

import com.ahl.server.entity.Match;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match,ObjectId> {

    public Match findFirstById(ObjectId matchId);

}
