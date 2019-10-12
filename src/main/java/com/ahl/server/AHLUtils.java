package com.ahl.server;

import com.ahl.server.exception.InvalidDataException;

import com.ahl.server.repository.MatchRepository;
import com.ahl.server.repository.PlayerRepository;
import com.ahl.server.repository.TeamRepository;
import com.ahl.server.repository.TournamentRepository;
import org.apache.commons.text.StringSubstitutor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class AHLUtils {

  public static boolean isValidEmailAddress(String email) throws InvalidDataException {

    java.util.regex.Pattern p = java.util.regex.Pattern.compile(AHLConstants.EMAIL_PATTERN);
    java.util.regex.Matcher m = p.matcher(email);
    if(m.matches()) {
      return true;
    }
    Map<String, String> substitueMap = new HashMap<>();
    substitueMap.put("fields", email);
    throw new InvalidDataException(AHLConstants.INVALID_DATA, substitueMap);
  }

  public static boolean isPlayerExist(PlayerRepository playerRepository, ObjectId playerId) throws InvalidDataException{
    if(playerRepository.findFirstById(playerId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_PLAYER);
    }
    return true;
  }
  public static boolean isTeamExist(TeamRepository teamRepository,ObjectId teamId) throws InvalidDataException{
    if(teamRepository.findFirstById(teamId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_TEAM);
    }
    return true;
  }

  public static boolean isMatchExist(MatchRepository matchRepository, ObjectId matchId) throws InvalidDataException {
    if(matchRepository.findFirstById(matchId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_MATCH);
    }
    return true;
  }
  public static boolean isTournamentExist(TournamentRepository tournamentRepository, ObjectId matchId) throws InvalidDataException {
    if(tournamentRepository.findFirstById(matchId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_TOURNAMENT);
    }
    return true;
  }
}
