package com.ahl.server.repository;

import com.ahl.server.entity.Card;
import com.ahl.server.entity.Goal;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, ObjectId> {

}
