package com.ahl.server.repository;

import com.ahl.server.entity.Player;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, String> {

    public Player findFirstById(ObjectId playerId);

}


