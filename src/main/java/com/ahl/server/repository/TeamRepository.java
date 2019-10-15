package com.ahl.server.repository;

import com.ahl.server.entity.Player;
import com.ahl.server.entity.Team;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, ObjectId> {

  public Team findFirstById(ObjectId id);

}


