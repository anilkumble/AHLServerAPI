package com.ahl.server.entity;


import com.ahl.server.AHLConstants;
import com.ahl.server.exception.InSufficientDataException;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.MultiValueMap;

import java.util.StringJoiner;

@Document(collection = "players")
public class Player {

  @Id
  private ObjectId id;
  private String name;
  private String email_id;

  public Player(String name, String email_id) {
    this.name = name;
    this.email_id = email_id;
  }

  public ObjectId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail_id() {
    return email_id;
  }

  public static boolean validatePlayer(MultiValueMap<String,String> playerData ) throws InSufficientDataException {

    if( ObjectUtils.anyNotNull(
        playerData.getFirst(AHLConstants.NAME),
        playerData.getFirst(AHLConstants.EMAIL_ID)))
    {
      throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS);
    }
    return true;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .add("email_id='" + email_id + "'")
        .toString();
  }
}
