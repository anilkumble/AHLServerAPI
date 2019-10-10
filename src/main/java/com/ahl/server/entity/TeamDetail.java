package com.ahl.server.entity;

import org.bson.types.ObjectId;

import java.util.StringJoiner;

public class TeamDetail {

  private ObjectId tournamentId;
  private ObjectId teamId;


  public ObjectId getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(ObjectId tournamentId) {
    this.tournamentId = tournamentId;
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
        .add("tournamentId=" + tournamentId)
        .add("teamId=" + teamId)
        .toString();
  }
}
