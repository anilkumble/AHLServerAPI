package com.ahl.server;

public class AHLConstants {

  public static final String NAME           = "name";
  public static final String POSITION       = "position";
  public static final String EMAIL_ID       = "emailId";
  public static final String PHONE_NO       = "phoneNo";
  public static final String GRADUATED_YEAR = "graduatedYear";
  public static final String GENDER         = "gender";
  public static final String DOB            = "dob";
  public static final String LEAGUE_ID      = "leagueId";
  public static final String PLAYER_LIST    = "playerList";



  /*To throw exception*/
  public static final String MINIMUM_REQUIRED_FIELDS = "Minimum Required Fields are {$fields}";
  public static final String INVALID_DATA            = "Invalid data {$fields}";

  public static final String PLAYER_CREATED   = "Player created successfully";
  public static final String PLAYER_UPDATED   = "Player updated successfully";
  public static final String PLAYER_DELETED   = "Player deleted successfully";
  public static final String PLAYER_NOT_FOUND = "Player not found";

  public static final String TEAM_CREATED     = "Team created successfully";
  public static final String TEAM_UPDATED     = "Team updated successfully";
  public static final String TEAM_DELETED     = "Team deleted successfully";

  public static final String LEAGUE_CREATED   = "League created successfully";
  public static final String LEAGUE_UPDATED   = "League updated successfully";
  public static final String LEAGUE_DELETED   = "League deleted successfully";




  public static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

  public static final String ERROR      = "error";
  public static final String ERROR_MSG  = "Error while persisting information";
  public static final String SUCCESS    = "success";



}
