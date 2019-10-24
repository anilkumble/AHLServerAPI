package com.ahl.server.repository;

import com.ahl.server.entity.Goal;
import com.ahl.server.entity.Points;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointsRepository extends CrudRepository<Points, ObjectId> {

    public Points findFirstById(ObjectId id);

    public Points findFirstByTeamId(ObjectId teamId);

}


