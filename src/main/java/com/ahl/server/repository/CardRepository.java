package com.ahl.server.repository;

import com.ahl.server.entity.Card;
import com.ahl.server.entity.Goal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, ObjectId> {

    public Card findFirstById(ObjectId cardId);

    @Query(value = "{ 'playerId' : ?0}")
    public List<Card> findCardsByplayerId(ObjectId id);

    @Query(value = "{'forTeamId' : ?0}")
    public List<Card> findCardsByTeamId(ObjectId TeamId);


}
