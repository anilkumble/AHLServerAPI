package com.ahl.server.repository;

import com.ahl.server.entity.Player;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, ObjectId> {

}


