package com.ahl.server.entity;

import org.bson.types.ObjectId;

import java.util.StringJoiner;

public class TeamDetail {

  private ObjectId leagueId;
  private ObjectId teamId;

  public ObjectId getLeagueId() {
    return leagueId;
  }

  public void setLeagueId(ObjectId leagueId) {
    this.leagueId = leagueId;
  }

  public ObjectId getTeamId() {
    return teamId;
  }

  public void setTeamId(ObjectId teamId) {
    this.teamId = teamId;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TeamDetail.class.getSimpleName() + "[", "]")
        .add("leagueId=" + leagueId)
        .add("teamId=" + teamId)
        .toString();
  }
}
