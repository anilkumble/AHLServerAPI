package com.ahl.server;

public class AHLConstants {

  public static final String NAME           = "name";
  public static final String POSITION       = "position";
  public static final String EMAIL_ID       = "emailId";
  public static final String PHONE_NO       = "phoneNo";
  public static final String GRADUATED_YEAR = "graduatedYear";
  public static final String GENDER         = "gender";
  public static final String DOB            = "dob";
  public static final String SEASON         = "season";
  public static final String TAG_LINE       = "tagline";
  public static final String PLAYER_LIST    = "playerList";

/*Goal*/

  public static final String FORTEAMID        = "forTeamId";
  public static final String AGAINSTTEAMID    = "againstTeamId";
  public static final String MATCHID          = "matchID";
  public static final String PLAYERID         = "playerId";
  public static final String LEAGUEID         = "leagueId";

  /*TeamTag category */
  public static final String MEN            = "men";
  public static final String ALL            = "all";
  public static final String WOMEN          = "women";
  public static final String OTHER          = "other";

  public static final String START  = "start";
  public static final String END    = "end";

  /*To throw exception*/
  public static final String MINIMUM_REQUIRED_FIELDS = "Minimum Required Fields are ${fields}";
  public static final String INVALID_DATA            = "Invalid data ${fields}";
  public static final String INVALID_PLAYER            = "Invalid Player";
  public static final String INVALID_TEAM            = "Invalid Team";
  public static final String INVALID_MATCH            = "Invalid Match";
  public static final String INVALID_TOURNAMENT            = "Invalid Tournament";
  public static final String TEAM_NOT_FOUND_IN_MATCH   = "Team not found in the given match";

  public static final String PLAYER_CREATED   = "Player created successfully";
  public static final String PLAYER_UPDATED   = "Player updated successfully";
  public static final String PLAYER_DELETED   = "Player deleted successfully";
  public static final String PLAYER_NOT_FOUND = "Player not found";

  public static final String PLAYER_RELATION_CREATED   = "Player Relation created successfully";

  public static final String TEAM_CREATED     = "Team created successfully";
  public static final String TEAM_UPDATED     = "Team updated successfully";
  public static final String TEAM_DELETED     = "Team deleted successfully";
  public static final String TEAM_NOT_FOUND   = "Team not found";

  public static final String TOURNAMENT_CREATED   = "Tournament created successfully";
  public static final String TOURNAMENT_UPDATED   = "Tournament updated successfully";
  public static final String TOURNAMENT_DELETED   = "Tournament deleted successfully";
  public static final String TOURNAMENT_NOT_FOUND = "Tournament not found";

  public static final String MATCH_CREATED   = "Match created successfully";
  public static final String MATCH_UPDATED   = "Match updated successfully";
  public static final String MATCH_DELETED   = "Match deleted successfully";
  public static final String MATCH_STARTED   = "Match started successfully";
  public static final String MATCH_ENDED     = "Match ended successfully";
  public static final String MATCH_NOT_FOUND = "Match not found";


  public static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

  public static final String ERROR      = "error";
  public static final String ERROR_MSG  = "Error while persisting information";
  public static final String SUCCESS    = "success";

/*For Goal */
  public static final String GOAL_CREATED ="Goal created successfully";
  public static final String GOAL_DELETED ="Goal deleted successfully";
  public static final String GOAL_UPDATED ="Goal updated successfully";
  public static final String GOAL_NOT_FOUND ="Goal not found";

  /*For Card */
  public static final String CARD_CREATED ="Card created successfully";
  public static final String CARD_DELETED ="Card deleted successfully";
  public static final String CARD_UPDATED ="Card updated successfully";
  public static final String CARD_NOT_FOUND ="Card not found";
}
