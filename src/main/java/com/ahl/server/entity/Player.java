package com.ahl.server.entity;


import com.ahl.server.AHLConstants;
import com.ahl.server.enums.Position;
import com.ahl.server.exception.InSufficientDataException;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Document(collection = "players")
public class Player {

    @Id
    private ObjectId id;
    private String name;
    private Position position;
    private String profile;

    public Player() {
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public static void validatePlayer(Player player) throws Exception {
        if (!ObjectUtils.allNotNull(player.getName(), player.getPosition())
                || ObjectUtils.isEmpty(player.getName()) || ObjectUtils.isEmpty(player.getPosition())) {

            Map<String, String> substitueMap = new HashMap<>();
            substitueMap.put("fields", Arrays.toString(new Object[]{AHLConstants.NAME, AHLConstants.POSITION}));
            throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS, substitueMap);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("position=" + position)
                .add("profile='" + profile + "'")
                .toString();
    }
}


