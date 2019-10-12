package com.ahl.server.repository;

import com.ahl.server.entity.Player;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerRepository extends CrudRepository<Player, String> {

  public Player findFirstById(ObjectId playerId);

  public Player findFirstByEmailId(String emailId);

  public Player findFirstByName(String name);

  @Query(value = "{ 'teamDetails.tournamentId' : ?0 }")
  List<Player> findPlayersByTournamentId(ObjectId tournamentId);

  @Query(value = "{ 'teamDetails.teamId' : ?0 }")
  List<Player> findPlayersByteamId(ObjectId teamId);

  @Query(value = "{ 'teamDetails.tournamentId' : ?0, 'teamDetails.teamId' : ?1 }")
  List<Player> findPlayersByournamentAndTeamId(ObjectId tournamentId, ObjectId teamId);


}


