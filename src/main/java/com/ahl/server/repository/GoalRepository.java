package com.ahl.server.repository;

import com.ahl.server.entity.Goal;
import com.ahl.server.entity.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GoalRepository extends MongoRepository<Goal, ObjectId> {



}


