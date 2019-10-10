package com.ahl.server.repository;

import com.ahl.server.entity.Tournament;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TournamentRepository extends MongoRepository<Tournament, ObjectId> {

  public Tournament findFirstById(ObjectId id);

}


