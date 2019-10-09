package com.ahl.server.repository;

import com.ahl.server.entity.League;
import com.ahl.server.entity.Player;
import com.ahl.server.entity.Team;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueRepository extends MongoRepository<League, ObjectId> {

  public League findFirstById(ObjectId id);

}


