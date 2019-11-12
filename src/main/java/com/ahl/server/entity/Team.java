package com.ahl.server.entity;


import com.ahl.server.AHLConstants;
import com.ahl.server.enums.TeamTag;
import com.ahl.server.exception.InSufficientDataException;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Document(collection = "teams")
public class Team {

  @Id
  private ObjectId id;
  private String name;
  private String teamLogo;
  private TeamTag teamTag;
  private ObjectId tournamentId;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TeamTag getTeamTag() {
    return teamTag;
  }

  public void setTeamTag(TeamTag teamTag) {
    this.teamTag = teamTag;
  }

  public String getTeamLogo() {
    return teamLogo;
  }

  public void setTeamLogo(String teamLogo) {
    this.teamLogo = teamLogo;
  }

  public ObjectId getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(ObjectId tournamentId) {
    this.tournamentId = tournamentId;
  }

  public static void validateTeam(Team team) throws InSufficientDataException {

    if(! ObjectUtils.allNotNull(team.getName(), team.getTeamTag(), team.getTournamentId())
            || ObjectUtils.isEmpty(team.getName()) || ObjectUtils.isEmpty(team.getTeamTag()) || ObjectUtils.isEmpty(team.getTournamentId())) {
      Map<String, String> substitueMap = new HashMap<>();
      substitueMap.put("fields", team.getName());
      throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS, substitueMap);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Team.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name)
        .toString();
  }
}
