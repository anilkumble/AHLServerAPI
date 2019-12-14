package com.ahl.server.repository;

import com.ahl.server.entity.Tournament;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, ObjectId> {

  public Tournament findFirstById(ObjectId id);

  @Query(value = "{'isLive' : ?0}")
  public Tournament findLiveTournament(boolean isLive);

}


