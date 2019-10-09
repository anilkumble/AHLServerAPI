package com.ahl.server.repository;

import com.ahl.server.entity.Player;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, ObjectId> {

  public Player findFirstByEmailId(String emailId);

  public Player findFirstByName(String name);

  @Query(value = "{ 'teamDetails.leagueId' : ?0 }")
  List<Player> findPlayersByLeagueId(ObjectId leagueId);

}


