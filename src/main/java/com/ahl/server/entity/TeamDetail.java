package com.ahl.server.entity;

import org.bson.types.ObjectId;

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
}
