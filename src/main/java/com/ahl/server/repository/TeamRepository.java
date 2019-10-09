package com.ahl.server.repository;

import com.ahl.server.entity.Player;
import com.ahl.server.entity.Team;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, ObjectId> {

  public Team findFirstById(ObjectId id);

}


