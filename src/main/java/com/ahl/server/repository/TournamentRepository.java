package com.ahl.server.repository;

import com.ahl.server.entity.Tournament;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, ObjectId> {

  public Tournament findFirstById(ObjectId id);

}


